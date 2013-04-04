package lucene;

import java.io.File;
import java.io.IOException;

import lucene.similarities.CustomSimilarities;
import lucene.similarities.TFIDFCustomSimilarity;
import lucene.similarities.VSMCustomSimilarity;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
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

	public void run(String[] args) throws ParseException, IOException {
		parser = new QueryParser(Version.LUCENE_42,
				LuceneParameterSchema.PARAM_SYNOPSIS, analyzer);
		Query query = parser.parse(args[args.length - 1]);
		CustomSimilarities.setCustomSimilarity(isearcher, args);
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;

		// Iterate through the results:
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			System.out
					.println(hitDoc.get(LuceneParameterSchema.PARAM_TITLE));
		}
		ireader.close();
	}

}
