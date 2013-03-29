package downloaders;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonNode;



public abstract class Downloader {
	protected URIBuilder uriBuilder = new URIBuilder();
	protected final String apiKey;
	protected final String path;
	protected HttpClient httpClient = new DefaultHttpClient();
	public Downloader(String path, String apiKey)
	{
		this.path = path;
		this.apiKey = apiKey;
		uriBuilder.setPath(this.path);
	}
	protected abstract void prepareParams();
	protected abstract HttpResponse executeRequest() throws URISyntaxException, ClientProtocolException, IOException; 
	public abstract JsonNode download();

}
