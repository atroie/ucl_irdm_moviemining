package text;

import java.util.regex.Pattern;

public class TextNormalizer {
	public static String normalize(String s)
	{
		String nospace = s.replaceAll("\\s+", " ");
		String lower = nospace.toLowerCase();
		return lower;
	}
}
