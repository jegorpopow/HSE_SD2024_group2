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

    public Map<String, String> getContext() {
        return context;
    }

    String processRawToken(String rawToken) {
        if (rawToken.charAt(0) == '\'') {
            return rawToken.substring(1, rawToken.length() - 1).replaceAll("\\\\'", "'");
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
            return buffer.toString();
        }

    }

    static List<String> splitArgs(String line) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"']\\S*|\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*')\\s*").matcher(line.trim());
        while (m.find()) list.add(m.group(1));
        return list;
    }

    public List<CallSpec> parseCalls(String line) {
        List<CallSpec> result = new ArrayList<>();
        List<String> call = new ArrayList<>();
        List<String> tokens = splitArgs(line)
                .stream()
                .filter(str -> !str.isEmpty())
                .map(this::processRawToken)
                .toList();

        System.out.println(tokens);
        for (String token : tokens) {
            if (token.equals("|")) {

                result.add(new CallSpec(call));
                call = new ArrayList<>();
            } else {
                call.add(token);
            }
        }

        result.add(new CallSpec(call));
        return result;
    }
}
