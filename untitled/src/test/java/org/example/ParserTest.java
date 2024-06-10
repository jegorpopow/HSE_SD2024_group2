package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    void testSplitWithQuotes() {
        Parser parser = new Parser();
        Assertions.assertEquals(List.of("a", "b", "c"), Parser.splitArgs("a b c"));
        Assertions.assertEquals(List.of("a", "\"b d\"", "c"), Parser.splitArgs("a \"b d\" c"));
        Assertions.assertEquals(List.of("a", "'b \"d\"'"), Parser.splitArgs("a 'b \"d\"'"));
        Assertions.assertEquals(List.of("a", "\"\\\"b\\\"\""), Parser.splitArgs("a \"\\\"b\\\"\""));
        Assertions.assertEquals(List.of("a", "\"\\\"b\""), Parser.splitArgs("a \"\\\"b\""));
    }

    @Test
    void testTokenQuotesProcessing() {
        Parser parser = new Parser();
        Assertions.assertEquals("abc", parser.processRawToken("abc"));
        Assertions.assertEquals("b d", parser.processRawToken("\"b d\""));
        Assertions.assertEquals("b \"d\"", parser.processRawToken("'b \"d\"'"));
        Assertions.assertEquals("\"b\"", parser.processRawToken("\"\\\"b\\\"\""));
        Assertions.assertEquals("\"b", parser.processRawToken("\"\\\"b\""));
    }

    @Test
    void testVars() {
        Parser parser = new Parser();
        parser.setVariable("a", "meow");
        parser.setVariable("b", "woof");
        Assertions.assertEquals("a", parser.processRawToken("a"));
        Assertions.assertEquals("meow", parser.processRawToken("$a"));
        Assertions.assertEquals("meowwoof", parser.processRawToken("$a$b"));
        Assertions.assertEquals("", parser.processRawToken("$undefined"));
        Assertions.assertEquals("meow b", parser.processRawToken("\"$a b\""));
        Assertions.assertEquals("meow woof", parser.processRawToken("\"$a $b\""));
        Assertions.assertEquals("$a b", parser.processRawToken("'$a b'"));
    }

    @Test
    void testPipeSplitting() {
        Parser parser = new Parser();
        Assertions.assertEquals(List.of(new CallSpec("a", "b"), new CallSpec("c", "d")), parser.parseCalls("a b | c d"));
        Assertions.assertEquals(List.of(new CallSpec("a", "b")), parser.parseCalls("a b"));
    }


}