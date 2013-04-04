package lucene.similarities;

import lucene.LuceneParameterSchema;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;

public class CustomSimilarities {

	public static void setCustomSimilarity(IndexSearcher isearcher, String[] args) {
		String model = args[0];
		if (model.equalsIgnoreCase(LuceneParameterSchema.PARAM_TF_IDF)) {
			 isearcher.setSimilarity(new TFIDFCustomSimilarity());
		} else if (model.equalsIgnoreCase(LuceneParameterSchema.PARAM_VSM)) {
			 isearcher.setSimilarity(new VSMCustomSimilarity());
		} else if (model.equalsIgnoreCase(LuceneParameterSchema.PARAM_BM25)) {
			isearcher.setSimilarity(new BM25Similarity(Float.valueOf(args[1]),
					Float.valueOf(args[2])));
		} else if (model.equalsIgnoreCase(LuceneParameterSchema.PARAM_JELINEK)) {
			isearcher.setSimilarity(new LMJelinekMercerSimilarity(Float
					.valueOf(args[1])));
		} else if (model.equalsIgnoreCase(LuceneParameterSchema.PARAM_DIRICH)) {
			isearcher.setSimilarity(new LMDirichletSimilarity(Float
					.valueOf(args[1])));
		}
	}

}
