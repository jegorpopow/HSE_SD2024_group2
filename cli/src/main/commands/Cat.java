package main.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public record Cat(String filename) implements Command {

    @Override
    public void execute() {
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
