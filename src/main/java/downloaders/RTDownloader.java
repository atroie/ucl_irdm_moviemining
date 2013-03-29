package downloaders;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

public abstract class RTDownloader extends Downloader {
	
	protected static final String RTKey = "8bvecvxyf2et7cu73fpu76wd";
	public RTDownloader(String path)
	{
		super(path,RTKey);
		uriBuilder.setParameter(RTRequestParameterSchema.PARAM_API_KEY, this.apiKey);
	}
	protected HttpResponse executeRequest() throws URISyntaxException, ClientProtocolException, IOException
	{
		prepareParams();
		HttpGet get = new HttpGet(uriBuilder.build());
		return httpClient.execute(get);
	}
}
