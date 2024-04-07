package org.example.builtins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Wc {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: wc <FILES>");
            System.exit(1);
        }

        for (String arg : args) {
            wc(arg);
        }
    }

    private static void wc(String filename) {
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

}
