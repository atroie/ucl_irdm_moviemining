package couchdbinteraction;


import java.util.List;

import org.codehaus.jackson.JsonNode;

import download.MovieDownloader;

public class FillCouchDB {

	/**
	 * @param args
	 */
	
	public static void usage()
	{
		System.err.println("Usage: <dbname>");
		System.err.println("Where <dbname> is the name of the database you want to upload to");
	}
	public static void main(String[] args) {
		if(args.length != 1)
		{
			usage();
			System.exit(-1);
		}
		System.out.println("Downloading movies...");
		MovieDownloader md = new MovieDownloader();
		List<JsonNode> movies = md.downloadMovies();
		System.out.printf("%d movies downloaded, inserting in database '%s'\n...",movies.size(),args[0]);
		CouchDBUploader up = new CouchDBUploader();
		up.uploadMoviesToDb(movies, args[0]);
	}
}


