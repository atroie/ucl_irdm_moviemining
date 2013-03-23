package download;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonNode;

/*import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;*/

import downloaders.Downloader;
import downloaders.RTListDownloader;
import downloaders.RTMovieDownloader;
import downloaders.RTResponseParameterSchema;
import downloaders.TopLevelRTListDownloader;

import org.ektorp.*;
import org.ektorp.impl.*;
import org.ektorp.http.*;



public class MovieDownloader {

	/**
	 * @param args
	 */

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