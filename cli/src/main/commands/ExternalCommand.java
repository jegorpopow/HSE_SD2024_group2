package main.commands;

import java.io.IOException;

public record ExternalCommand(String[] commands) implements Command {

    @Override
    public void execute() {
        try {
            Process process = new ProcessBuilder(commands).inheritIO().start();
            int exitCode = process.waitFor();
            System.out.println("External command exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing command: " + e.getMessage());
        }
    }
}
