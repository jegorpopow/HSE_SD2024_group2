package org.example.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;

public record RunConfig(String command, String[] args, InputStream in, PrintStream out, PrintStream err) {
}
