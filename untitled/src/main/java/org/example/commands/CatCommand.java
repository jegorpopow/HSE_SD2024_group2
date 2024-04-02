package org.example.commands;

import java.io.*;

public class CatCommand implements Command {
    @Override
    public int run(RunConfig config) throws CommandError {
        if (config.args().length < 1) {
            config.out().println("Usage: cat <FILE>");
            return 2;
        }

        for (String fileName : config.args()) {
            cat(fileName, config.out(), config.err());
        }

        return 0;
    }

    private void cat(String fileName, PrintStream out, PrintStream err) throws CommandError {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException e) {
            err.println("Error reading file: " + e.getMessage());
        }
    }
}
