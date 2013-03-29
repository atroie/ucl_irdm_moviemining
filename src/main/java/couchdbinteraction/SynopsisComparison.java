package couchdbinteraction;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.codehaus.jackson.JsonNode;
import org.ektorp.CouchDbConnector;

public class SynopsisComparison {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CouchDbConnector rt = CouchDBFactory.getConnection("movies");
		CouchDbConnector om = CouchDBFactory.getConnection("omdb");
		List<String> rtIds = rt.getAllDocIds();
		List<String> omIds = om.getAllDocIds();
		List<Integer> rtLen = new ArrayList<Integer>();
		List<Integer> omLen = new ArrayList<Integer>();
		int wcrt=0,wcom=0;
		for(String id : rtIds)
		{
			JsonNode movie = rt.get(JsonNode.class, id);
			StringTokenizer tok = new StringTokenizer(movie.get("synopsis").getTextValue());
			Integer len = tok.countTokens();
			wcrt += len;
			rtLen.add(len);
		}
		
		for(String id : omIds)
		{
			JsonNode movie = om.get(JsonNode.class, id);
			StringTokenizer tok = new StringTokenizer(movie.get("overview").getTextValue());
			Integer len = tok.countTokens();
			wcom += len;
			omLen.add(len);
		}
		double avgrt = (double)wcrt/(double)rtIds.size();
		double avgom = (double)wcom/(double)omIds.size();
		double varrt=0,varom=0;
		for(Integer i : rtLen)
		{
			varrt+=(i-avgrt)*(i-avgrt);
		}
		for(Integer i : omLen)
		{
			varom+=(i-avgom)*(i-avgom);
		}
		varrt /= rtLen.size()-1;
		varom /= omLen.size()-1;
		System.out.printf("%f %f\n",avgrt,avgom);
		System.out.printf("%f %f\n",varrt,varom);

	}

}
