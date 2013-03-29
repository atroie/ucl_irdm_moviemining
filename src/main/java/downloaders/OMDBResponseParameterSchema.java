package downloaders;

import parameterschema.ParameterSchema;

public interface OMDBResponseParameterSchema extends ParameterSchema {
	public static final String PARAM_GENRE_LIST = "genres";
	public static final String PARAM_TOTAL_PAGES = "total_pages";
	public static final String PARAM_RESULTS = "results";
	public static final String PARAM_ID = "id";
	public static final String PARAM_TITLE = "title";
}
