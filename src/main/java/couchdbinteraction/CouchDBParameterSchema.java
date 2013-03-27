package couchdbinteraction;

import parameterschema.ParameterSchema;

public interface CouchDBParameterSchema extends ParameterSchema {
	public static final String PARAM_ID = "_id";
	public static final String PARAM_REV = "_rev";
	public static final String PARAM_RT_ID = "id";
	public static final String PARAM_TITLE =  "title";
	public static final String PARAM_SYNOPSIS = "synopsis";
	public static final String MOVIE_DB = "movies";
}
