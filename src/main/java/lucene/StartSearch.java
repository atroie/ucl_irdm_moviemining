package lucene;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

public class StartSearch {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		//Params: DIR to export index
		Search search = new Search("C:\\Users\\Elston\\Documents\\CS\\4\\Information Retrieval\\Group\\indexes");
		
		//Params: fieldName,Query
		search.run("title", "Alex Cross");
	}

}
