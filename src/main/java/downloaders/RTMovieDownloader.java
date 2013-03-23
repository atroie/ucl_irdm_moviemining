package downloaders;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

public class RTMovieDownloader extends RTDownloader {

	public RTMovieDownloader(String path) {
		super(path);
	}

	@Override
	public JsonNode download() {
		try {
			uriBuilder.setParameter(RTRequestParameterSchema.PARAM_PAGE,"1");
			HttpResponse resp = executeRequest();
			ObjectMapper mp = new ObjectMapper();
			JsonNode root = mp.readTree(resp.getEntity().getContent());
			if(root.has(RTResponseParameterSchema.PARAM_MOVIES))
			{
				JsonNode movies = root.get(RTResponseParameterSchema.PARAM_MOVIES);
				if(root.has(RTResponseParameterSchema.PARAM_TOTAL))
				{
					int total = root.get(RTResponseParameterSchema.PARAM_TOTAL).asInt();
					if(total > RTRequestParameterSchema.PARAM_PAGE_LIMIT_DEFAULT)
					{
						int q = RTRequestParameterSchema.PARAM_PAGE_LIMIT_DEFAULT;
						int remainingDownload = total - q;
						int extraRequests = (remainingDownload / q) + (remainingDownload % q == 0 ? 0 : 1);
						for(int r = 0; r < extraRequests; r++)
						{
							uriBuilder.setParameter(RTRequestParameterSchema.PARAM_PAGE, String.format("%d",r+2));
							HttpResponse rr = this.executeRequest();
							JsonNode extraMovies = mp.readTree(rr.getEntity().getContent());
							if(extraMovies.has(RTResponseParameterSchema.PARAM_MOVIES))
							{
								for(JsonNode jn : extraMovies.get(RTResponseParameterSchema.PARAM_MOVIES))
								{
									
									((ArrayNode)movies).add(jn);
								}
							}
						}
						
					}
				}
				return root;
			}
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

	@Override
	protected void prepareParams() {
		uriBuilder.setParameter(RTRequestParameterSchema.PARAM_LIMIT, RTRequestParameterSchema.PARAM_LIMIT_DEFAULT.toString());
		uriBuilder.setParameter(RTRequestParameterSchema.PARAM_PAGE_LIMIT, RTRequestParameterSchema.PARAM_PAGE_LIMIT_DEFAULT.toString());
		//uriBuilder.setParameter(RTRequestParameterSchema.PARAM_PAGE,"1");
		// TODO Auto-generated method stub
		
	}

}
