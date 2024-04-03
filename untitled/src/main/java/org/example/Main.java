package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

            if (calls.size() == 1) {

                List<String> tokens = calls.getFirst();

                // String[] tokens = input.split("\\s+");
                String command = tokens.get(0);

                switch (command) {
                    case "cat":
                        if (tokens.size() < 2) {
                            System.out.println("Usage: cat <FILE>");
                            break;
                        }
                        cat(tokens.get(1));
                        break;
                    case "echo":
                        echo(Arrays.copyOfRange(tokens.toArray(new String[0]), 1, tokens.size()));
                        break;
                    case "wc":
                        if (tokens.size() < 2) {
                            System.out.println("Usage: wc <FILE>");
                            break;
                        }
                        wc(tokens.get(1));
                        break;
                    case "pwd":
                        pwd();
                        break;
                    case "exit":
                        return;
                    default:
                        if (command.contains("=")) {
                            String[] parts = command.trim().split("=");
                            parser.setVariable(parts[0], parts[1]);
                        } else {
                            executeExternalCommand(tokens.toArray(new String[0]));
                        }
                }
            } else {
                try {
                    List<Process> processes = ProcessBuilder.startPipeline(createProcesses(calls, parser.getContext()));
                    for (Process process : processes) {
                        process.waitFor();
                    }
                } catch (IOException | InterruptedException e) {
                    System.out.println("Problem with pipes:(");
                }
            }
        }
    }

    static private List<ProcessBuilder> createProcesses(List<List<String>> calls, Map<String, String> environment) {
        List<ProcessBuilder> result = new ArrayList<>();
        for (int i = 0; i < calls.size(); i++) {
            ProcessBuilder processBuilder = new ProcessBuilder(calls.get(i).toArray(new String[0])).inheritIO();
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

    public static void cat(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void echo(String[] args) {
        for (String arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();
    }

    public static void wc(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int lines = 0;
            int words = 0;
            int bytes = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lines++;
                bytes += line.getBytes().length;
                String[] wordsArray = line.split("\\s+");
                words += wordsArray.length;
            }
            System.out.println(lines + " " + words + " " + bytes + " " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void pwd() {
        System.out.println(System.getProperty("user.dir"));
    }

    private static void executeExternalCommand(String[] command) {
        try {
            Process process = new ProcessBuilder(command).inheritIO().start();
            int exitCode = process.waitFor();
            System.out.println("External command exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing command: " + e.getMessage());
        }
    }
}
