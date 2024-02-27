package main;

import java.io.*;
import java.util.*;

public class CommandLineInterpreter {
    private static final String PROMPT = "> ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print(PROMPT);
            input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "cat":
                    if (tokens.length < 2) {
                        System.out.println("Usage: cat <FILE>");
                        break;
                    }
                    cat(tokens[1]);
                    break;
                case "echo":
                    echo(Arrays.copyOfRange(tokens, 1, tokens.length));
                    break;
                case "wc":
                    if (tokens.length < 2) {
                        System.out.println("Usage: wc <FILE>");
                        break;
                    }
                    wc(tokens[1]);
                    break;
                case "pwd":
                    pwd();
                    break;
                case "exit":
                    return;
                default:
                    executeExternalCommand(tokens);
            }
        }
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
