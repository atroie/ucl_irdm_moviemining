package downloaders;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

public class OMDBSingleMovieDownloader extends OMDBDownloader {

	private static final String pathTemplate = "http://api.themoviedb.org/3/movie/@id";
	public OMDBSingleMovieDownloader(String movieId) {
		super(pathTemplate.replaceFirst("@id",movieId));
	}

	@Override
	protected void prepareParams() {
		//nothing to do

	}

	@Override
	public JsonNode download() {
		try {
			HttpResponse resp = null;
			resp = this.executeRequest();
			ObjectMapper om = new ObjectMapper();
			return om.readTree(resp.getEntity().getContent());
		} catch (ClientProtocolException e) {
		
			e.printStackTrace();
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
		} 
		  catch(JsonParseException e) {
			  e.printStackTrace();
		  }
		catch (IOException e) {
			
			e.printStackTrace();
		} 
		return null;
	}

}
