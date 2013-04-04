package lucene;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ektorp.CouchDbConnector;

import couchdbinteraction.CouchDBFactory;

public class BuildQrels {

	private static void usage() {
		System.err.println("Usage: <qrels-excel-file> <qrels-output-file> <dbname>");
		System.err.println("Where <qrels-excel-file> is an excel file containing the relevant docs for each query");
		System.err.println("PLEASE NOTE: this must be an XLSX file, NOT XLS");
		System.err.println("Where <qrels-output-file> is the output file where you want the qrels to be written (e.g. qrels.txt)");
		System.err.println("Where <dbname> is the name of the db containing all the movie ids");
		
	}
	
	private static List<String> docIds = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 3)
		{
			usage();
			System.exit(-1);
		}
		OPCPackage pack = null;
		Workbook wb = null;
		try {
			getDocIds(args[2]);
			pack = OPCPackage.open(args[0], PackageAccess.READ);
			wb = new XSSFWorkbook(pack);
			for(int i = 1;i < wb.getNumberOfSheets(); i++)
			{
				Sheet s = wb.getSheetAt(i);
				System.out.println(s.getSheetName());
				Set<String> hs = new HashSet<String>();
				for(int j = 0; j <= s.getLastRowNum(); j++)
				{
					Row r = s.getRow(j);
					Cell idCell = r.getCell(2);
					hs.add(idCell.getStringCellValue());
				}
				writeQrels(i-1,hs,args[1]);
			}
			pack.revert();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			pack.revert(); //close the pack and revert any (?) changes
		}

	}
	private static void getDocIds(String dbName) {
		CouchDbConnector inst = CouchDBFactory.getConnection(dbName);
		docIds = inst.getAllDocIds();
		
	}
	/**
	 * Please note that this assumes that the topics have the same order both in the excel and the topics file
	 * @param topicNo
	 * @param hs
	 * @param string
	 */
	private static void writeQrels(int topicNo, Set<String> relevantIds, String filePath) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath,topicNo != 0)));
			for(String id : docIds)
			{
				pw.println(String.format("%d 0 %s %d",topicNo,id,relevantIds.contains(id) ? 1 : 0 ));
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		finally
		{
			pw.close();
		}
		
		
	}
	
	

	

}
