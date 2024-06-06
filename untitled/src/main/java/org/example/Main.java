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
    final static Set<String> builtins = Set.of("cat", "wc", "echo", "pwd");
    final static String path_to_lib = System.getenv("JAVA_BUILD");

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

            List<CallSpec> calls = parser.parseCalls(input);
            CallSpec call = calls.getFirst();

            if (call.isExit())
                return;

            Optional<CallSpec.AssignSpec> assignSpec = call.assignment();

            if (calls.size() == 1 && assignSpec.isPresent()) {
                parser.setVariable(assignSpec.get().variable, assignSpec.get().value);
                continue;
            }

            try {
                List<Process> processes = ProcessBuilder.startPipeline(createProcesses(calls, parser.getContext()));
                for (Process process : processes) {
                    process.waitFor();
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Problem with external commands call");
            }
        }
    }

    static private List<ProcessBuilder> createProcesses(List<CallSpec> calls, Map<String, String> environment) {
        List<ProcessBuilder> result = new ArrayList<>();
        for (int i = 0; i < calls.size(); i++) {
            CallSpec call = calls.get(i);
            if (builtins.contains(call.command)) {

                String command = call.command;
                var head = List.of("java", "-cp", path_to_lib, "org.example.builtins.".concat(command.substring(0, 1).toUpperCase() + command.substring(1)));
                call = new CallSpec(Stream.concat(head.stream(), call.arguments.stream()).toList());
            }

            ProcessBuilder processBuilder = new ProcessBuilder(call.toArray()).inheritIO();
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
