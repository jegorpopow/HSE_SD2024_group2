import main.CliCommandsParser;
import main.ParserResult;
import main.commands.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CliCommandsParserTest {

    @Test
    void testCatParse() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "cat file.txt";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Assertions.assertInstanceOf(Cat.class, actualResult.command());
        Assertions.assertNull(actualResult.sideEffect());
    }

    @Test
    void testCatParseWithoutArgs() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "cat";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Assertions.assertNull(actualResult.command());
        Assertions.assertEquals("Usage: cat <FILE>", actualResult.sideEffect().errorMessage());
    }

    @Test
    void testPwdParse() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "pwd";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Assertions.assertInstanceOf(Pwd.class, actualResult.command());
        Assertions.assertNull(actualResult.sideEffect());
    }

    @Test
    void testEchoParse() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "echo";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Assertions.assertInstanceOf(Echo.class, actualResult.command());
        Assertions.assertNull(actualResult.sideEffect());
    }

    @Test
    void testEchoParseWithArg() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "echo kek";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Command actualCommand = actualResult.command();
        Assertions.assertInstanceOf(Echo.class, actualResult.command());

        Assertions.assertEquals(1, ((Echo) actualCommand).args().length);
        Assertions.assertEquals("kek", ((Echo) actualCommand).args()[0]);

        Assertions.assertNull(actualResult.sideEffect());
    }

    @Test
    void testEchoParseWithArgs() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "echo kek lol";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Command actualCommand = actualResult.command();
        Assertions.assertInstanceOf(Echo.class, actualResult.command());

        Assertions.assertEquals(2, ((Echo) actualCommand).args().length);
        Assertions.assertEquals("kek", ((Echo) actualCommand).args()[0]);
        Assertions.assertEquals("lol", ((Echo) actualCommand).args()[1]);

        Assertions.assertNull(actualResult.sideEffect());
    }

    @Test
    void testExit() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "exit";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Assertions.assertInstanceOf(Exit.class, actualResult.command());
        Assertions.assertNull(actualResult.sideEffect());
    }

    @Test
    void testWcParse() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "wc testFile.txt";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Assertions.assertInstanceOf(Wc.class, actualResult.command());
        Assertions.assertNull(actualResult.sideEffect());
    }

    @Test
    void testWcParseWithoutArgs() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "wc";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Assertions.assertNull(actualResult.command());
        Assertions.assertEquals("Usage: wc <FILE>", actualResult.sideEffect().errorMessage());
    }

    @Test
    void testExternalCommand() {
        // precondition
        CliCommandsParser parser = new CliCommandsParser();
        String input = "unknownCommand";

        // action
        ParserResult actualResult = parser.parse(input);

        // assertion
        Assertions.assertInstanceOf(ExternalCommand.class, actualResult.command());
        Assertions.assertNull(actualResult.sideEffect());
    }
}
