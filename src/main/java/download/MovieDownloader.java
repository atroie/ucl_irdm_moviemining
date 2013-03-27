package download;


import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import downloaders.Downloader;
import downloaders.RTListDownloader;
import downloaders.RTMovieDownloader;
import downloaders.RTResponseParameterSchema;
import downloaders.TopLevelRTListDownloader;




public class MovieDownloader {


	public List<JsonNode> downloadMovies() {
		Downloader d = new TopLevelRTListDownloader();
		JsonNode node = d.download();
		List<JsonNode> downloadedMovies = new ArrayList<JsonNode>();
		for(JsonNode iTopLink : node.get(RTResponseParameterSchema.PARAM_LINKS))
		{
			Downloader d2 = new RTListDownloader(iTopLink.getTextValue());
			JsonNode subList = d2.download();
			for(JsonNode iSubList : subList.get(RTResponseParameterSchema.PARAM_LINKS))
			{
				Downloader md = new RTMovieDownloader(iSubList.getTextValue());
				JsonNode mvs = md.download();
				for(JsonNode iMovie : mvs.get(RTResponseParameterSchema.PARAM_MOVIES))
				{
					downloadedMovies.add(iMovie);
				}
				
			}
		}
		return downloadedMovies;
		
		


	}

}