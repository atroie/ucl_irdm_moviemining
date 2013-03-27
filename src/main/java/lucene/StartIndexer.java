package lucene;

import java.io.File;
import java.io.IOException;

public class StartIndexer {

	public static void usage()
	{
		System.err.println("Usage: <dbname> <indexdir> [wipeindex]");
		System.err.println("<dbname> is the name of the database to be indexed");
		System.err.println("<indexdir> is the name where you want to create the index");
		System.err.println("wipeindex: (optional) whether to clear the index directory before proceeding");
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if(args.length < 2)
		{
			usage();
			System.exit(-1);
		}
		File indexDir = new File(args[1]);
		if(!indexDir.exists())
			indexDir.mkdirs();
		if(args.length == 3 && Boolean.parseBoolean(args[2]) == true)
		{
			for(File child : indexDir.listFiles())
			{
				if(child.getName().equals("..") || child.getName().equals("."))
				{
					child.delete();
				}
			}
		}
		System.out.printf("Indexing database '%s' into directory %s\n",args[0],args[1]);
		Indexer indexer = new Indexer(args[1]);
		indexer.buildIndices(args[0]);
	}

}
