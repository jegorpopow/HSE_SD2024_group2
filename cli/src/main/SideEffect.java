package main;

public record SideEffect(String errorMessage) {

    public void run() {
        System.out.println(errorMessage);
    }
}
