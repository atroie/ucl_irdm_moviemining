package couchdbinteraction;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import download.MovieDownloader;

public class FillCouchDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MovieDownloader md = new MovieDownloader();
		CouchDBUploader up = new CouchDBUploader();
		up.upload(md.downloadMovies(), "movies");
	}
}


