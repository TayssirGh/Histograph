package com.tsts.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CodingStatsServiceTest {
    private CodingStatsService codingStatsService;

    @BeforeEach
    void setUp() {
        codingStatsService = new CodingStatsService();
    }
    @Test
    void testDisplayCodingHours() throws IOException {
        String logData = "2024-12-08 08:00:00 START\n" +
                "2024-12-08 12:00:00 END\n" +
                "2024-12-09 09:00:00 START\n" +
                "2024-12-09 11:00:00 END\n";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        InputStream logInputStream = new ByteArrayInputStream(logData.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(logInputStream));
        codingStatsService.displayCodingHours();
        String output = outputStream.toString();
        assertTrue(output.contains("Coding Hours Curve"));
        assertTrue(output.contains("*"));
    }

}