package couchdbinteraction;

import java.util.Hashtable;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbPath;
import org.ektorp.Revision;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDBFactory {
	private static CouchDbInstance dbInstance = new StdCouchDbInstance(new StdHttpClient.Builder().build());
	private static Hashtable<String,CouchDbConnector> connections = new Hashtable<String,CouchDbConnector>();
	
	/**
	 * Returns a connection to the database indicated by the parameter databaseName; if the database does not exist it is created
	 * @param databaseName
	 * @return A connection to the database indicated by the parameter databaseName
	 */
	public static CouchDbConnector getConnection(String databaseName)
	{
		if(!connections.containsKey(databaseName))
		{
			connections.put(databaseName, dbInstance.createConnector(databaseName, true));
		}
		return connections.get(databaseName);
	}
	/**
	 * Deletes the database given
	 * @param databaseName The database to be deleted
	 * @return True if and only if the database exists and was deleted; false otherwise
	 */
	public static boolean deleteDatabase(String databaseName)
	{
		if(dbInstance.checkIfDbExists(new DbPath(databaseName)))
		{
			dbInstance.deleteDatabase(databaseName);
			if(connections.containsKey(databaseName))
				connections.remove(databaseName);
			return true;
		}
		return false;
	}
	/**
	 * Check whether a given database exists in the CouchDB system
	 * @param databaseName The database to check
	 * @return True if and only if the given database exists
	 */
	public static boolean databaseExists(String databaseName)
	{
		return dbInstance.checkIfDbExists(new DbPath(databaseName));
	}
	
	/**
	 * Clears all the entries with all their revisions in the given database
	 * @param The database to be cleared
	 * @return True if and only if the database exists and has been cleared
	 */
	public static boolean clearDatabase(String databaseName)
	{
		if(dbInstance.checkIfDbExists(new DbPath(databaseName)))
		{
			CouchDbConnector con = getConnection(databaseName);
			List<String> allIds = con.getAllDocIds();
			for(String id : allIds)
			{
				List<Revision> itemRevs = con.getRevisions(id);
				for(Revision rev : itemRevs)
				{
					con.delete(id,rev.getRev());
				}
			}
			return true;
		}
		return false;
	}
	

}
