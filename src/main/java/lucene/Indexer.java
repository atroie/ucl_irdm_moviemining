package lucene;

import java.io.File;
import java.io.IOException;
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

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.ektorp.CouchDbConnector;


import couchdbinteraction.CouchDBFactory;
import couchdbinteraction.CouchDBParameterSchema;

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

			if (CouchDBParameterSchema.PARAM_ID.equals(fieldname)) {
				filmParser.nextToken();
				String id = filmParser.getText();
				doc.add(new Field(LuceneParameterSchema.PARAM_ID, id, TextField.TYPE_STORED));
			}
			if (CouchDBParameterSchema.PARAM_TITLE.equals(fieldname)) {
				filmParser.nextToken();
				String title = filmParser.getText();
				doc.add(new Field(LuceneParameterSchema.PARAM_TITLE, title, TextField.TYPE_STORED));
			}
			if (CouchDBParameterSchema.PARAM_SYNOPSIS.equals(fieldname)) {
				filmParser.nextToken();
				String synopsis = filmParser.getText();
				doc.add(new Field(LuceneParameterSchema.PARAM_SYNOPSIS, synopsis, TextField.TYPE_STORED));
			}

		}
		indexWriter.addDocument(doc);
	}

	public void indexFilm(JsonNode film) throws JsonParseException, IOException {
		Document doc = new Document();		
		doc.add(new Field(LuceneParameterSchema.PARAM_ID, film.get(CouchDBParameterSchema.PARAM_ID).getTextValue(), TextField.TYPE_STORED));
		doc.add(new Field(LuceneParameterSchema.PARAM_TITLE, film.get(CouchDBParameterSchema.PARAM_TITLE).getTextValue(), TextField.TYPE_STORED));
		doc.add(new Field(LuceneParameterSchema.PARAM_SYNOPSIS,film.get(CouchDBParameterSchema.PARAM_SYNOPSIS).getTextValue(),TextField.TYPE_STORED));
		indexWriter.addDocument(doc);
	}

	public void buildIndices(String dbName) throws IOException {
		// CouchDB Set up

		CouchDbConnector db = CouchDBFactory.getConnection(dbName);

		getIndexWriter();

		List<String> filmIDs = db.getAllDocIds();
		for (String filmID : filmIDs) {
			JsonNode film = db.get(JsonNode.class, filmID);
					indexFilm(film);
		}

		closeIndexWriter();
	}
}
