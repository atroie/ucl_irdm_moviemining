package couchdbinteraction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import text.TextNormalizer;

public class KeyUtil {
	/**
	 * This method returns a key from the given title. It converts it to lowercase, normalises all whitespace
	 * and then calculates the SHA-512 hash of it to get the key
	 * @param title A string representing the title
	 * @return The SHA-512 hash of the normalized form of the title as a key
	 */
	public static String getKeyForTitle(String title)
	{

		
		try {
			String nTitle = TextNormalizer.normalize(title); 
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] hash = md.digest(nTitle.getBytes());
			String key = Hex.encodeHexString(hash);
			return key;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
}
