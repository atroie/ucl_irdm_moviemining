package couchdbinteraction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.ektorp.CouchDbConnector;
import org.ektorp.UpdateConflictException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class CouchDBMigration {
	/**
	 * Exports the database indicated by dbName into the file indicated by filePath
	 * @param dbName The database to be exported
	 * @param filePath The file to export to 
	 */
	public static void exportDB(String dbName,String filePath)
	{

		try {
			File f = new File(filePath);
			OutputStream os = new FileOutputStream(f);
			CouchDbConnector db = CouchDBFactory.getConnection(dbName);
			if(db == null)
			{
				os.close();
				throw new IllegalArgumentException("Database does not exist");
			}


			ArrayNode allMovies = JsonNodeFactory.instance.arrayNode();
			for(String itemId : db.getAllDocIds())
			{
				JsonNode item = db.get(JsonNode.class, itemId);
				ObjectNode on = (ObjectNode)item;
				on.remove("_rev"); //this needs more work
				allMovies.add(on);
			}
			os.write(allMovies.toString().getBytes());
			os.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Exports a representation of the given database to an excel file
	 * @param dbName The name of the database to export
	 * @param filePath The file to export to
	 */
	public static void exportDBToExcel(String dbName, String filePath) {
		SXSSFWorkbook wb = new SXSSFWorkbook(1); // number of rows to keep in memory; I'm only writing one row at a time so 1 is enough
		String[] fields = new String[] { CouchDBParameterSchema.PARAM_TITLE,CouchDBParameterSchema.PARAM_SYNOPSIS,CouchDBParameterSchema.PARAM_ID };
		CouchDbConnector db = CouchDBFactory.getConnection(dbName);
		Sheet sh = wb.createSheet();
		int i = 0;
		Row header = sh.createRow(0);
		for(; i < fields.length; i++)
		{
			Cell headerCell = header.createCell(i);
			headerCell.setCellValue(fields[i]);
		}
		int rownum = 1;
		for(String id : db.getAllDocIds())
		{
			JsonNode movie = db.get(JsonNode.class, id);
			Row row = sh.createRow(rownum++);
			for(i = 0;i < fields.length;i++)
			{
				Cell cell = row.createCell(i);
				cell.setCellValue(movie.get(fields[i]).getTextValue());
			}
			
		}
		
		try 
		{
			OutputStream out = new FileOutputStream(filePath); 
			wb.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			// dispose of temporary files backing this workbook on disk
			wb.dispose();

		}


	}

	/**
	 * Import a list of movies into the given database from the given filepath
	 * @param dbName The database to import to
	 * @param filePath The file to import from
	 * @param clearDb Specify whether to clear the database before importing
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static void importDB(String dbName,String filePath,boolean clearDb) 
			throws JsonProcessingException, IOException
			{
		if(clearDb)
		{
			CouchDBFactory.clearDatabase(dbName);
		}
		CouchDbConnector db = CouchDBFactory.getConnection(dbName);
		ObjectMapper om = new ObjectMapper();
		File inp = new File(filePath);
		InputStream is = new FileInputStream(inp);
		ArrayNode movies = (ArrayNode)om.readTree(is);
		for(JsonNode movie : movies)
		{
			try
			{
				db.create(movie);
			}
			catch(UpdateConflictException uce)
			{
				System.out.printf("Item with id %s already exists (%s)\n",movie.get("_id"),movie.get("title"));
			}
		}
		is.close();
			}

	public static void usage()
	{
		System.err.println("usage: <command> <dbname> <filename> [deleteonimport]");
		System.err.println("command: one of 'import', 'export' or 'excel-export'");
		System.err.println("dbname: name of database that you are importing from/exporting to");
		System.err.println("filename: name of file to import from/export to");
		System.err.println("deleteonimport: (optional) one of 'true' or 'false'. If 'true', wipe database clean before import");
	}

	public static void main(String args[]) throws JsonProcessingException, IOException
	{
		if(args.length < 3)
		{
			usage();
			System.exit(-1);
		}
		if(args[0].equals("export"))
		{
			System.out.printf("Exporting database '%s' to file %s\n",args[1],args[2]);
			exportDB(args[1],args[2]);
		}
		else if(args[0].equals("import"))
		{
			boolean clearDb = (args.length > 3 && Boolean.parseBoolean(args[3]) == true);
			System.out.printf("Importing movies from file %s to database '%s'\n",args[2],args[1]);
			importDB(args[1],args[2],clearDb);
		}
		else if(args[0].equals("excel-export"))
		{
			System.out.printf("Exporting database '%s' to file %s\n",args[1],args[2]);
			exportDBToExcel(args[1],args[2]);
		}
		else
		{
			System.err.println("Unrecognised command");
			System.exit(-2);
		}
	}


}
