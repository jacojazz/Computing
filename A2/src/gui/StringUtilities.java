package gui;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringUtilities {
		public static List<String> wrap(String str, FontMetrics fm, int maxWidth) {
		List<String> lines = splitIntoLines(str);
		if (lines.size() == 0)
			return lines;

		ArrayList<String> strings = new ArrayList<String>();
		for (Iterator<String> iter = lines.iterator(); iter.hasNext();)
			wrapLineInto((String) iter.next(), strings, fm, maxWidth);
		return strings;
	}

	public static void wrapLineInto(String line, List<String> list, FontMetrics fm, int maxWidth) {
		int len = line.length();
		int width;
		while (len > 0 && (width = fm.stringWidth(line)) > maxWidth) {
			int guess = len * maxWidth / width;
			String before = line.substring(0, guess).trim();

			width = fm.stringWidth(before);
			int pos;
			if (width > maxWidth)
				pos = findBreakBefore(line, guess);
			else {
				pos = findBreakAfter(line, guess);
				if (pos != -1) { 
					before = line.substring(0, pos).trim();
					if (fm.stringWidth(before) > maxWidth)
						pos = findBreakBefore(line, guess);
				}
			}
			if (pos == -1)
				pos = guess;

			list.add(line.substring(0, pos).trim());
			line = line.substring(pos).trim();
			len = line.length();
		}
		if (len > 0)
			list.add(line);
	}


	public static int findBreakBefore(String line, int start) {
		for (int i = start; i >= 0; --i) {
			char c = line.charAt(i);
			if (Character.isWhitespace(c) || c == '-')
				return i;
		}
		return -1;
	}

	public static int findBreakAfter(String line, int start) {
		int len = line.length();
		for (int i = start; i < len; ++i) {
			char c = line.charAt(i);
			if (Character.isWhitespace(c) || c == '-')
				return i;
		}
		return -1;
	}

	public static List<String> splitIntoLines(String str) {
		ArrayList<String> strings = new ArrayList<String>();

		int len = str.length();
		if (len == 0) {
			strings.add("");
			return strings;
		}

		int lineStart = 0;

		for (int i = 0; i < len; ++i) {
			char c = str.charAt(i);
			if (c == '\r') {
				int newlineLength = 1;
				if ((i + 1) < len && str.charAt(i + 1) == '\n')
					newlineLength = 2;
				strings.add(str.substring(lineStart, i));
				lineStart = i + newlineLength;
				if (newlineLength == 2)
					++i;
			} else if (c == '\n') {
				strings.add(str.substring(lineStart, i));
				lineStart = i + 1;
			}
		}
		if (lineStart < len)
			strings.add(str.substring(lineStart));

		return strings;
	}

}