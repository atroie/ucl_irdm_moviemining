package couchdbinteraction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbPath;
import org.ektorp.UpdateConflictException;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDBMigration {
	public static void exportDB(String filePath,String dbName)
	{
		
		try {
			File f = new File(filePath);
			OutputStream os = new FileOutputStream(f);
			HttpClient httpClient = new StdHttpClient.Builder().build();
			CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
			CouchDbConnector db = dbInstance.createConnector(dbName, false);
			if(db == null) throw new IllegalArgumentException("Database does not exist");
			ArrayNode allMovies = JsonNodeFactory.instance.arrayNode();
			for(String itemId : db.getAllDocIds())
			{
				JsonNode item = db.get(JsonNode.class, itemId);
				ObjectNode on = (ObjectNode)item;
				on.remove("_rev"); //this needs more work
				allMovies.add(on);
			}
			os.write(allMovies.toString().getBytes());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void importDB(String filePath,String dbName,boolean deleteOldDb) throws JsonProcessingException, IOException
	{
		HttpClient httpClient = new StdHttpClient.Builder().build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		if(deleteOldDb && dbInstance.checkIfDbExists(new DbPath(dbName)))
		{
			dbInstance.deleteDatabase(dbName);
		}
		CouchDbConnector db = dbInstance.createConnector(dbName, true);
		ObjectMapper om = new ObjectMapper();
		File inp = new File(filePath);
		InputStream is = new FileInputStream(inp);
		ArrayNode movies = (ArrayNode)om.readTree(is);
		for(JsonNode movie : movies)
		{
			try
			{
			db.create(movie);
			}
			catch(UpdateConflictException uce)
			{
				System.out.printf("Item with id %s already exists (%s)\n",movie.get("_id"),movie.get("title"));
			}
		}
		is.close();
	}
	
	public static void main(String args[])
	{
		//sorry for the poor state of the code; for now you can use this method to try out those two methods above; 
		//I plan to add a command line interface some time when I can think straighter
		exportDB("/home/atroie/movies.json","movies");
	}

}
