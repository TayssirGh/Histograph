package com.tsts.presentation;

import com.tsts.services.CodingStatsService;
import picocli.CommandLine;

@CommandLine.Command(
        name = "coding-stats",
        description = "Analyze coding hours and determine the most active coding hour."
)
public class CodingStatsCommand implements Runnable {


    @Override
    public void run() {
        try {
            CodingStatsService codingStatsService = new CodingStatsService();
            codingStatsService.displayCodingHours();
        } catch (Exception e) {
            System.err.println("Error analyzing coding hours: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
