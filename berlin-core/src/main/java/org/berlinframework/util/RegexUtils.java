package org.berlinframework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Abhilash Krishnan
 */
public class RegexUtils {

    public static boolean match(String s, String p) {
        Matcher m = Pattern.compile(p).matcher(s);
        return m.find() ? true : false;
    }

    public static String getMatch(String s, String p) {
        Matcher m = Pattern.compile(p).matcher(s);
        return m.find() ? m.group(0) : null;
    }

    public static List<String> getMatches (String s, String p) {
        List<String> matches = new ArrayList<>();
        Matcher m = Pattern.compile(p).matcher(s);
        while (m.find())
            matches.add(m.group(1));
        return matches;
    }

    public static String[] positiveLookAheadMatches(String s, String lookFor) {
        String pattern = "(?=" + lookFor + ")";
        return s.split(pattern);
    }
}
