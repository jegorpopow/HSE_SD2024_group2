package org.example.builtins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Echo {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: echo <text>");
            System.exit(1);
        }

        echo(args);

    }

        public static void echo(String[] args) {
            for (String arg : args) {
                System.out.print(arg + " ");
            }
            System.out.println();
        }
}
