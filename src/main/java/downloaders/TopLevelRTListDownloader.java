package downloaders;

public class TopLevelRTListDownloader extends RTListDownloader {
	private static final String topLevelPath = "http://api.rottentomatoes.com/api/public/v1.0/lists.json";
	
	public TopLevelRTListDownloader()
	{
		super(topLevelPath);
	}

}
