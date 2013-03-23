package couchdbinteraction;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.UpdateConflictException;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import downloaders.RTResponseParameterSchema;

public class CouchDBUploader {
	/**
	 * Inserts a list of movies represented as JSON entities into the given database
	 * If the database doesn't exist, it is created
	 * @param movies a list of JsonNodes, as returned by, e.g. MovieDownloader
	 * @param dbName name of the database to upload to
	 */
	public void upload(List<JsonNode> movies,String dbName)
	{
		HttpClient httpClient = new StdHttpClient.Builder().build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector db = dbInstance.createConnector(dbName, true);
		int successfullyInserted = 0;
		for(JsonNode movie : movies)
		{
			String movieId = KeyUtil.getKeyForTitle(movie.get(RTResponseParameterSchema.PARAM_TITLE).getTextValue());
			try
			{
			db.create(movieId,movie);
			System.out.printf("Inserted %s\n",movie.get(RTResponseParameterSchema.PARAM_TITLE).getTextValue());
			successfullyInserted++;
			}
			catch(UpdateConflictException uce)
			{
				System.out.printf("Item with id %s already exists (%s)\n",movieId,movie.get(RTResponseParameterSchema.PARAM_TITLE).getTextValue());
			}
		}
		System.out.printf("Received %d movies, inserted %d\n",movies.size(),successfullyInserted);
	}

	
}
