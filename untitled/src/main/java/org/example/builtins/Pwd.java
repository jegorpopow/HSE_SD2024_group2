package org.example.builtins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Pwd {
    public static void main(String[] args) {
        if (args.length != 0) {
            System.out.println("Usage: pwd ");
            System.exit(1);
        }

        pwd();
    }

    private static void  pwd() {
        System.out.println(System.getProperty("user.dir"));
    }


}
