package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class Main {

    private static String getPrompt() {
        return System.getProperty("user.dir") + "$ ";
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print(getPrompt());
            input = scanner.nextLine().trim();

            if (input.trim().isEmpty()) {
                continue;
            }

            List<List<String>> calls = parser.parseCalls(input);
            List<String> tokens = calls.getFirst();
            String command = tokens.get(0);


            if (command.equals("exit"))
                return;

            if (calls.size() == 1 && command.contains("=")) {
                String[] parts = command.trim().split("=");
                parser.setVariable(parts[0], parts[1]);
                continue;
            }

            try {
                List<Process> processes = ProcessBuilder.startPipeline(createProcesses(calls, parser.getContext()));
                for (Process process : processes) {
                    // TODO: print return value if it is only one command in pipe
                    process.waitFor();
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Problem with pipes:(");
            }
        }
    }

    static private List<ProcessBuilder> createProcesses(List<List<String>> calls, Map<String, String> environment) {
        List<ProcessBuilder> result = new ArrayList<>();
        for (int i = 0; i < calls.size(); i++) {
            List<String> call = calls.get(i);
            Set <String> builtins = Set.of("cat", "wc", "echo", "pwd");
            if (builtins.contains(call.getFirst()) ) {
                String path_to_lib = System.getenv("JAVA_BUILD");
                String command = call.getFirst();
                call.removeFirst();
                var head = List.of("java", "-cp", path_to_lib, "org.example.builtins.".concat(command.substring(0, 1).toUpperCase() + command.substring(1)));
                call = Stream.concat(head.stream(), call.stream()).toList();
            }


            ProcessBuilder processBuilder = new ProcessBuilder(call.toArray(new String[0])).inheritIO();
            if (i != 0) {
                processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
            }

            if (i != calls.size() - 1) {
                processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
            }

            Map<String, String> processEnvironment = processBuilder.environment();
            processEnvironment.putAll(environment);
            result.add(processBuilder);
        }
        return result;
    }
}