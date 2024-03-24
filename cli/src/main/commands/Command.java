package main.commands;

public sealed interface Command permits Cat, Pwd, Wc, Echo, Exit, ExternalCommand {

    void execute();
}
