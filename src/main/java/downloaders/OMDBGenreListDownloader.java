package downloaders;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class OMDBGenreListDownloader extends OMDBDownloader{

	private static final String genreListPath = "http://api.themoviedb.org/3/genre/list";
	public OMDBGenreListDownloader()
	{
		super(genreListPath);
	}
	@Override
	protected void prepareParams() {
		//nothing to do
		
	}
	@Override
	public JsonNode download() {
		
		try {
			HttpResponse resp = executeRequest();
			ObjectMapper mp = new ObjectMapper();
			JsonNode root = mp.readTree(resp.getEntity().getContent());
			return root.get(OMDBResponseParameterSchema.PARAM_GENRE_LIST);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
