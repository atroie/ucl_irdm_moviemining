package lucene;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.queryparser.classic.ParseException;

public class StartSearch {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	private static void usage() {
		System.err
				.println("Usage: <index-dir> <search-method> [<method-param> [<method-param]] <search-query>");
		System.err
				.println("Where <index-dir> is the directory where the index lives");
		System.err
				.println("<search-method> is one of 'tf-idf', 'vsm', 'bm25', 'jelinek', and 'dirich'");
		System.err
				.println("<search-query> is the query you want to search for");
		System.err
				.println("If you want a multi-term query, it must be included in double quotes");
		System.err.println("e.g. \"Alex Cross\"");
	}

	public static void main(String[] args) throws IOException, ParseException {
		if (!(args.length > 2 && args.length < 6)) {
			usage();
			System.exit(-1);
		}
		Search search = new Search(args[0]);

		// Params: fieldName,Query
		System.out.printf(
				"Searching for query term '%s'\nUsing model '%s' \nUsing index directory %s\n",
				args[args.length - 1], args[1], args[0]);
		search.run(Arrays.copyOfRange(args, 1, args.length));
	}

}
