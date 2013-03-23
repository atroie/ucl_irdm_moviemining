package downloaders;

public abstract class RTDownloader extends Downloader {
	
	protected static final String RTKey = "8bvecvxyf2et7cu73fpu76wd";
	public RTDownloader(String path)
	{
		super(path,RTKey);
		uriBuilder.setParameter(RTRequestParameterSchema.PARAM_API_KEY, this.apiKey);
	}
}
