package test;

import main.CommandLineInterpreter;
import org.junit.jupiter.api.Test;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class CommandLineInterpreterTest {
    @Test
    void testCat() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String testFileName = "testFile.txt";
        try (PrintWriter writer = new PrintWriter(testFileName)) {
            writer.println("Test line 1");
            writer.println("Test line 2");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CommandLineInterpreter.cat(testFileName);

        assertEquals("Test line 1" + System.lineSeparator() + "Test line 2" + System.lineSeparator(), outContent.toString());

        File fileToDelete = new File(testFileName);
        if (!fileToDelete.delete()) {
            System.err.println("Failed to delete temporary file");
        }
    }

    @Test
    void testEcho() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        CommandLineInterpreter.echo(new String[]{"hello", "world"});

        assertEquals("hello world \n", outContent.toString());
    }

    @Test
    void testWc() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String testFileName = "testFile.txt";
        try (PrintWriter writer = new PrintWriter(testFileName)) {
            writer.println("Test line 1");
            writer.println("Test line 2");
            writer.println("Test line 3");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CommandLineInterpreter.wc(testFileName);

        assertEquals("3 9 33 testFile.txt" + System.lineSeparator(), outContent.toString());

        // Удаляем временный файл
        File fileToDelete = new File(testFileName);
        if (!fileToDelete.delete()) {
            System.err.println("Failed to delete temporary file");
        }
    }

    @Test
    void testPwd() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        CommandLineInterpreter.pwd();

        assertEquals(System.getProperty("user.dir") + System.lineSeparator(), outContent.toString());
    }
}
