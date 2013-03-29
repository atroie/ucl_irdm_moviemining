package couchdbinteraction;


import download.OMDBMovieListDownloader;
import download.RottenTomatoesMovieListDownloader;
import downloaders.OMDBResponseParameterSchema;
import downloaders.RTResponseParameterSchema;

public class FillCouchDB {

	/**
	 * @param args
	 */
	
	public static void usage()
	{
		System.err.println("Usage: <source> <dbname>");
		System.err.println("Where <srouce> is one of 'omdb', 'rt'");
		System.err.println("Where <dbname> is the name of the database you want to upload to");
	}
	public static void main(String[] args) {
		if(args.length != 2)
		{
			usage();
			System.exit(-1);
		}
		CouchDBUploader up = new CouchDBUploader();
		if(args[0].equals("omdb"))
		{
			OMDBMovieListDownloader omld = new OMDBMovieListDownloader();
			up.uploadMoviesToDb(omld.downloadMovies(), args[1], OMDBResponseParameterSchema.PARAM_TITLE);
		}
		else if(args[0].equals("rt"))
		{
			RottenTomatoesMovieListDownloader rtmld = new RottenTomatoesMovieListDownloader();
			up.uploadMoviesToDb(rtmld.downloadMovies(), args[1], RTResponseParameterSchema.PARAM_TITLE);
		}
		
	} 
}


