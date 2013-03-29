package downloaders;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;


public abstract class OMDBDownloader extends Downloader {
	protected final static String OMDB_API_KEY = "d94aa36d076ffa388504437b9f46d5ab";
	public OMDBDownloader(String path)
	{
		super(path, OMDB_API_KEY);
		uriBuilder.setParameter(OMDBRequestParameterSchema.PARAM_API_KEY, this.apiKey);

	}

	protected HttpResponse executeRequest() throws URISyntaxException, ClientProtocolException, IOException
	{
		//prepareParams(); //not needed
		HttpGet get = new HttpGet(uriBuilder.build());
		get.setHeader("Accept", "application/json");
		HttpResponse resp = httpClient.execute(get);
		while(resp.getStatusLine().getStatusCode() >= 400)
		{
			try
			{
				EntityUtils.consume(resp.getEntity());
				System.out.println("Got an error response code, waiting 5 sec...");
				Thread.sleep(5000);
				resp = this.executeRequest();
			}
			catch(InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
		return resp;
	}

}
