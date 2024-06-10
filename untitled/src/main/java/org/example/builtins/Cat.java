package org.example.builtins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Cat {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: cat <FILES>");
            System.exit(1);
        }

        for (String arg : args) {
            cat(arg);
        }
    }

    private static void cat(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
