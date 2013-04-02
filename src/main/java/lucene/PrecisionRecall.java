package lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.apache.lucene.benchmark.quality.Judge;
import org.apache.lucene.benchmark.quality.QualityBenchmark;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.QualityStats;
import org.apache.lucene.benchmark.quality.trec.TrecJudge;
import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;
import org.apache.lucene.benchmark.quality.utils.SimpleQQParser;
import org.apache.lucene.benchmark.quality.utils.SubmissionReport;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class PrecisionRecall {

	private static void usage()
	{
		System.err.println("Usage: <topics-path> <qrels-path> <index-dir>");
		System.err.println("Where <index-dir> is the directory where the index lives");
		System.err.println("Where <topics-dir> is the file path of topics.txt");
		System.err.println("Where <qrels-dir> is the file path of qrels.txt");
	}
	
	public static void main(String[] args) throws Throwable {
		if(args.length != 3)
		{
			usage();
			System.exit(-1);
		}
		
		precisionRecall(args[0],args[1],args[2]);
	}
	
	private static void precisionRecall(String topicsPath, String qrelsPath, String indexDir) throws Exception {
		File topicsFile = new File(topicsPath);
		File qrelsFile = new File(qrelsPath);
		Directory dir = FSDirectory.open(new File(indexDir));
		DirectoryReader ireader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(ireader);

		String docNameField = LuceneParameterSchema.PARAM_ID;

		PrintWriter logger = new PrintWriter(System.out, true);

		TrecTopicsReader qReader = new TrecTopicsReader(); // #1
		QualityQuery qqs[] = qReader.readQueries( // #1
				new BufferedReader(new FileReader(topicsFile))); // #1

		Judge judge = new TrecJudge(new BufferedReader( // #2
				new FileReader(qrelsFile))); // #2

		judge.validateData(qqs, logger); // #3

		QualityQueryParser qqParser = new SimpleQQParser("title", LuceneParameterSchema.PARAM_SYNOPSIS); // #4

		QualityBenchmark qrun = new QualityBenchmark(qqs, qqParser, searcher,
				docNameField);
		SubmissionReport submitLog = null;
		QualityStats stats[] = qrun.execute(judge, // #5
				submitLog, logger);

		QualityStats avg = QualityStats.average(stats); // #6
		avg.log("SUMMARY", 2, logger, "  ");
		dir.close();
	}

}
