package downloaders;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;

public class OMDBGenreMoviesDownloader extends OMDBDownloader {

	private static final String pathTemplate = "http://api.themoviedb.org/3/genre/@id/movies";
	public OMDBGenreMoviesDownloader(String genreId) {
		super(pathTemplate.replaceFirst("@id", genreId));
	}

	@Override
	protected void prepareParams() {
		//nothing to do 

	}

	@Override
	public JsonNode download() {
		
		try { 
			uriBuilder.setParameter(OMDBRequestParameterSchema.PARAM_PAGE, "1");
			HttpResponse resp = this.executeRequest();
			ObjectMapper mp = new ObjectMapper();
			ArrayNode shortMovies = JsonNodeFactory.instance.arrayNode();
			JsonNode root = mp.readTree(resp.getEntity().getContent());
			for(JsonNode jm :  root.get(OMDBResponseParameterSchema.PARAM_RESULTS))
			{
				shortMovies.add(jm);
			}
			int numPages = Integer.parseInt(root.get(OMDBResponseParameterSchema.PARAM_TOTAL_PAGES).toString());
			if(numPages > 1)
			{
				for(int i = 2; i <= numPages; i++)
				{
					uriBuilder.setParameter(OMDBRequestParameterSchema.PARAM_PAGE,String.format("%d",i));
					HttpResponse resp2 = this.executeRequest();
					JsonNode root2 = mp.readTree(resp2.getEntity().getContent());
					for(JsonNode jn : root2.get(OMDBResponseParameterSchema.PARAM_RESULTS))
					{
						shortMovies.add(jn);
					}
				}
			}
			return shortMovies;
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
