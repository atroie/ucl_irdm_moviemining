package downloaders;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;



public class RTListDownloader extends RTDownloader {

	public RTListDownloader(String path) {
		super(path);
	}

	@Override
	public JsonNode download() {
		
		try {
			HttpResponse resp = executeRequest();
			ObjectMapper mp = new ObjectMapper();
			JsonNode root = mp.readTree(resp.getEntity().getContent());
			if(root.has(RTResponseParameterSchema.PARAM_LINKS)){
				return root;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void prepareParams() {
		// nothing needed
		
	}

}
