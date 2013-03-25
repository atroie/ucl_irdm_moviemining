package lucene;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class Indexer {

	private IndexWriter indexWriter = null;
	Directory directory;
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_42);

	public Indexer(String dirPath) {
		try {
			directory = FSDirectory.open(new File(dirPath));
		} catch (IOException e) {
			System.out.println("Directory not found!");
			e.printStackTrace();
		}
	}

	public void closeIndexWriter() throws IOException {
		if (indexWriter != null) {
			indexWriter.close();
		}
	}

	public IndexWriter getIndexWriter() throws IOException {
		if (indexWriter == null) {
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42,
					analyzer);
			indexWriter = new IndexWriter(directory, config);
		}
		return indexWriter;
	}

	public void indexFilm(JsonParser filmParser) throws JsonParseException, IOException {
		Document doc = new Document();
		
		while (filmParser.nextToken() != null) {			 
			String fieldname = filmParser.getCurrentName();
			
			if ("_id".equals(fieldname)) {
				filmParser.nextToken();
				String id = filmParser.getText();
				doc.add(new Field("id", id, TextField.TYPE_STORED));
			}
			if ("title".equals(fieldname)) {
				filmParser.nextToken();
				String title = filmParser.getText();
				doc.add(new Field("title", title, TextField.TYPE_STORED));
			}
			if ("synopsis".equals(fieldname)) {
				filmParser.nextToken();
				String synopsis = filmParser.getText();
				doc.add(new Field("synopsis", synopsis, TextField.TYPE_STORED));
			}

		}
		indexWriter.addDocument(doc);
	}

	public void buildIndices(String dbName) throws IOException {
		// CouchDB Set up
		HttpClient httpClient = new StdHttpClient.Builder().build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector db = dbInstance.createConnector(dbName, true);
		JsonFactory jfactory = new JsonFactory();

		getIndexWriter();

		List<String> filmIDs = db.getAllDocIds();
		for (String filmID : filmIDs) {
			InputStream filmStream = db.getAsStream(filmID);
			JsonParser jParser = jfactory.createJsonParser(filmStream);
			indexFilm(jParser);
		}

		closeIndexWriter();
	}
}
