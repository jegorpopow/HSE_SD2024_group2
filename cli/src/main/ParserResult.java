package main;

import main.commands.Command;

public record ParserResult(Command command, SideEffect sideEffect) {
}
