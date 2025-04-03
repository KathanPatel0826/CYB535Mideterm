package com.midterm.helloworld;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HelloworldApplicationTests {

    @Test
    void testMainOutput() {
        // Capture system output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Run the main method
        HelloworldApplication.main(new String[]{});

        // Restore original System.out
        System.setOut(originalOut);

        // Verify output (trimmed to remove extra spaces/newlines)
        assertEquals("Hello, World!", outputStream.toString().trim());
    }
}


