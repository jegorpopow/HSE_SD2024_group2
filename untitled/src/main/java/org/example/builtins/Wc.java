package org.example.builtins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Wc {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        if (args.length < 1) {
            System.out.println("Usage: cat <FILES>");
            System.exit(1);
        }

        for (int i = 1; i < args.length; i++) {
            cat(args[i]);
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
