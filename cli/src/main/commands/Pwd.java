package main.commands;

final public class Pwd implements Command {

    @Override
    public void execute() {
        System.out.println(System.getProperty("user.dir"));
    }
}
