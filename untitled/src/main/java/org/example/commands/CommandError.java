package org.example.commands;

public class CommandError extends Throwable {
    public CommandError(String message) {
        super(message);
    }
}