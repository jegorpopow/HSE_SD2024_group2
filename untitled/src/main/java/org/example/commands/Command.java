package org.example.commands;

public interface Command {
    int run(RunConfig config) throws CommandError;
}
