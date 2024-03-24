package main.commands;

public record Echo(String[] args) implements Command {

    @Override
    public void execute() {
        for (String arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();
    }
}
