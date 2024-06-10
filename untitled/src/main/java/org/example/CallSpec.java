package org.example;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


// Represents a one pipeline unit (one command)
public final class CallSpec {
    public final String command;
    public final List<String> arguments;

    public CallSpec(String command, List<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CallSpec otherSpec)) {
            return false;
        }

        return command.equals(otherSpec.command) && arguments.equals(otherSpec.arguments);
    }

    public CallSpec(List<String> tokens) {
        this(tokens.getFirst(), tokens.subList(1, tokens.size()));
    }

    public CallSpec(String command, String... args) {
        this(command, Arrays.asList(args));
    }

    /**
     * @return if corresponding command is exit command
     */
    public boolean isExit() {
        return command.equals("exit");
    }


    /**
     * Assignment command representation - contains lvalue and rvalue of assignment
     */
    public static class AssignSpec {
        final String variable;
        final String value;

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof AssignSpec otherSpec)) {
                return false;
            }

            return variable.equals(otherSpec.variable) && value.equals(otherSpec.value);
        }

        public AssignSpec(String variable, String value) {
            this.variable = variable;
            this.value = value;
        }
    }


    /**
     * @return the variable name and value to assign if corresponding command is assignment command,
     * otherwise - Optional.empty() is returned
     */
    public Optional<AssignSpec> assignment() {
        if (arguments.isEmpty() && command.contains("=")) {
            String[] parts = command.trim().split("=");
            return Optional.of(new AssignSpec(parts[0], parts[1]));
        } else {
            return Optional.empty();
        }
    }


    /**
     * @return corresponding command as array of tokens
     */
    public String[] toArray() {
        return Stream.concat(Stream.of(command), arguments.stream()).toArray(String[]::new);
    }
}
