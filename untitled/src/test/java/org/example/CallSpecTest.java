package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CallSpecTest {
    @Test
    void equals() {
        Assertions.assertEquals(new CallSpec("wc", "meow.txt"),
                new CallSpec("wc", "meow.txt"));
        Assertions.assertNotEquals(new CallSpec("wc", "meow.txt"),
                new CallSpec("wc", "meow.mp3"));
    }

    @Test
    void isExit() {
        Assertions.assertTrue(new CallSpec("exit").isExit());
        Assertions.assertTrue(new CallSpec("exit", "right now").isExit());
        Assertions.assertFalse(new CallSpec("pwd").isExit());
    }

    @Test
    void assignment() {
        Assertions.assertEquals(Optional.of(new CallSpec.AssignSpec("a", "b")),
                new CallSpec("a=b").assignment());
        Assertions.assertEquals(Optional.empty(),
                new CallSpec("a=b", "meow").assignment());
        Assertions.assertEquals(Optional.empty(),
                new CallSpec("pwd", "meow").assignment());
    }

    @Test
    void toArray() {
        String[] params = {"hello", "world", "meow"};
        Assertions.assertArrayEquals(params, new CallSpec("hello", "world", "meow").toArray());
        Assertions.assertArrayEquals(params, new CallSpec("hello", List.of("world", "meow")).toArray());
        Assertions.assertArrayEquals(params, new CallSpec(List.of("hello", "world", "meow")).toArray());
    }
}