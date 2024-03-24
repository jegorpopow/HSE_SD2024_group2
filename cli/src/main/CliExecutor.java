package main;

import main.commands.Command;
import main.commands.Exit;

import java.util.Scanner;

public class CliExecutor {

    private static final String PROMPT = "> ";

    private final Scanner scanner;

    private final CliCommandsParser commandsParser;

    public CliExecutor(
            Scanner scanner,
            CliCommandsParser commandsParser
    ) {
        this.scanner = scanner;
        this.commandsParser = commandsParser;
    }

    void run() {
        String input;
        while (true) {
            System.out.print(PROMPT);
            input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            ParserResult result = commandsParser.parse(input);
            Command command = result.command();
            SideEffect sideEffect = result.sideEffect();

            if (command instanceof Exit) return;

            // если у нас случился sideEffect, значит произошла ошибка и нужно вывести что-то на экран
            // когда появится кейс, где при сайд-эффекте исполняется команда, то перепишем эту логику)
            // а ещё, в идеальном мире - Exit - не команда, а sideEffect, как мне кажется...
            if (sideEffect != null) {
                sideEffect.run();
            } else {
                command.execute();
            }
        }
    }
}
