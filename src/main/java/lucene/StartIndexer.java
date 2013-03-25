package lucene;

import java.io.IOException;

public class StartIndexer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//Params: DIR to export index
		Indexer indexer = new Indexer("C:\\Users\\Elston\\Documents\\CS\\4\\Information Retrieval\\Group\\indexes");
		
		//Params: Your DB name
		indexer.buildIndices("IRFILM");
	}

}
