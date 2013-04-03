package lucene;

import parameterschema.ParameterSchema;

public interface LuceneParameterSchema extends ParameterSchema {
	public static final String INDEX_DIR = "data/index/";
	
	public static final String PARAM_TITLE = "title";
	public static final String PARAM_ID = "id";
	public static final String PARAM_SYNOPSIS = "synopsis";
	
	public static final String PARAM_TF_IDF = "tf-idf";
	public static final String PARAM_VSM = "vsm";
	public static final String PARAM_BM25 = "bm25";
	public static final String PARAM_JELINEK = "jelinek";
	public static final String PARAM_DIRICH = "dirich";
}
