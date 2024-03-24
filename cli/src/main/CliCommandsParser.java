package main;

import main.commands.*;

import java.util.Arrays;
import java.util.function.Function;

public class CliCommandsParser {

    private final Function<String, String[]> tokenizer = string -> string.split("\\s+");

    public CliCommandsParser() {
    }

    public ParserResult parse(String input) {
        String[] tokens = tokenizer.apply(input);
        SideEffect sideEffect = null;
        Command command = null;

        switch (tokens[0]) {
            case "cat" -> {
                if (tokens.length < 2) {
                    sideEffect = new SideEffect("Usage: cat <FILE>");
                    break;
                }
                command = new Cat(tokens[1]);
            }
            case "echo" -> {
                command = new Echo(Arrays.copyOfRange(tokens, 1, tokens.length));
            }
            case "wc" -> {
                if (tokens.length < 2) {
                    sideEffect = new SideEffect("Usage: wc <FILE>");
                    break;
                }
                command = new Wc(tokens[1]);
            }
            case "pwd" -> {
                command = new Pwd();
            }
            case "exit" -> {
                command = new Exit();
            }
            default -> {
                command = new ExternalCommand(tokens);
            }
        }
        return new ParserResult(command, sideEffect);
    }
}
