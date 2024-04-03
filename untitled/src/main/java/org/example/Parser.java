package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.text.StringEscapeUtils.unescapeJava;

public class Parser {
    private final Map<String, String> context = new HashMap<>();

    public void setVariable(String variable, String value) {
        context.put(variable, value);
    }

    List<String> parseArgs(String line) {
        List<String> splitted = splitArgs(line);
        List<String> result = new ArrayList<>(splitted.size());

        for (String rawToken : splitted) {
            if (rawToken.isEmpty()) {
                continue;
            }
            if (rawToken.charAt(0) == '\'') {
                result.add(rawToken.substring(1, rawToken.length() - 1).replaceAll("\\\\'", "'"));
            } else {
                if (rawToken.charAt(0) == '"') {
                    rawToken = unescapeJava(rawToken.substring(1, rawToken.length() - 1));
                }
                StringBuilder buffer = new StringBuilder();
                Matcher matcher = Pattern.compile("\\$([\\w_?]*)(?=$|[^\\w_?])").matcher(rawToken);
                while (matcher.find()) {
                    matcher.appendReplacement(buffer, context.getOrDefault(matcher.group(1), ""));
                }
                matcher.appendTail(buffer);
                result.add(buffer.toString());
            }
        }

        return result;
    }

    List<String> splitArgs(String line) {
        List<String> list = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"']\\S*|\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*')\\s*").matcher(line.trim());
        while (m.find()) list.add(m.group(1));
        return list;
    }
}
