package download;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import downloaders.Downloader;
import downloaders.OMDBGenreListDownloader;
import downloaders.OMDBGenreMoviesDownloader;
import downloaders.OMDBResponseParameterSchema;
import downloaders.OMDBSingleMovieDownloader;

public class OMDBMovieListDownloader {
	
	public List<JsonNode> downloadMovies()
	{
		List<JsonNode> movies = new ArrayList<JsonNode>();
		Downloader genreDownloader = new OMDBGenreListDownloader();
		for(JsonNode genreItem : genreDownloader.download())
		{
			System.out.printf("Downloading genre %s\n",genreItem.get("name").toString());
			Downloader iGenre = new OMDBGenreMoviesDownloader(genreItem.get(OMDBResponseParameterSchema.PARAM_ID).toString());
			for(JsonNode shortMovie : iGenre.download())
			{
				
				Downloader movieDownloader = new OMDBSingleMovieDownloader(shortMovie.get(OMDBResponseParameterSchema.PARAM_ID).toString());
				JsonNode fullMovie = movieDownloader.download();
				System.out.printf("Downloading movie %s\n",fullMovie.get("title").getTextValue());
				movies.add(fullMovie);
				//movies.add(movieDownloader.download());
			}
		}
		return movies;
	}
	

}
