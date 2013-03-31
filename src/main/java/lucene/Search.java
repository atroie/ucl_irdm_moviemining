package lucene;

import java.io.File;
import java.io.IOException;

import lucene.similarities.TFIDFCustom;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Search {
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_42);
	DirectoryReader ireader;
	Directory directory;
	QueryParser parser;
	IndexSearcher isearcher;

	public Search(String dirPath) throws IOException {
		directory = FSDirectory.open(new File(dirPath));
		ireader = DirectoryReader.open(directory);
		isearcher = new IndexSearcher(ireader);
	}

	public void run(String fieldName, String q) throws ParseException,
			IOException {
		parser = new QueryParser(Version.LUCENE_42, fieldName, analyzer);
		Query query = parser.parse(q);
		setCustomSimilarity();
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;

		// Iterate through the results:
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			System.out
					.println(hitDoc.get(LuceneParameterSchema.PARAM_SYNOPSIS));
		}
		ireader.close();
	}

	/**
	 * Method changes the Similarity used. Currently points to the TFIDF toy
	 * class.
	 */
	private void setCustomSimilarity() {
		isearcher.setSimilarity(new TFIDFCustom());
	}

}
