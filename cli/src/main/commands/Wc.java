package main.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public record Wc(String filename) implements Command {

    @Override
    public void execute() {
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
