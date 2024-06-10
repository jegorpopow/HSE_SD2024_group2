package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.text.StringEscapeUtils.unescapeJava;

public class Parser {
    private final Map<String, String> context = new HashMap<>();

    /**
     * <p>
     *     assigns a variable in current environment
     * </p>
     * @param variable  the name of variable
     * @param value - the new value of variable
     */
    public void setVariable(String variable, String value) {
        context.put(variable, value);
    }

    /**
     * @return the current environment
     */
    public Map<String, String> getContext() {
        return context;
    }

    /**
     * <p> Perform unsescaping and variable substitution, removes quotes
     * </p>
     * @param rawToken token to process
     * @return the token after listed operations
     */
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

    /**
     * <p> Split a line to tokens (raw ones) with respect to quotes
     * </p>
     * @param line -  line consisting of tokens
     * @return the list of tokens
     */
    static List<String> splitArgs(String line) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"']\\S*|\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*')\\s*").matcher(line.trim());
        while (m.find()) list.add(m.group(1));
        return list;
    }

    /**
     * <p> performs tokenization, process tokens and parses a pipes
     * </p>
     * @param line - command to parse
     * @return the list of CallSpec - pipe elements
     */
    public List<CallSpec> parseCalls(String line) {
        List<CallSpec> result = new ArrayList<>();
        List<String> call = new ArrayList<>();
        List<String> tokens = splitArgs(line)
                .stream()
                .filter(str -> !str.isEmpty())
                .map(this::processRawToken)
                .toList();
        
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
