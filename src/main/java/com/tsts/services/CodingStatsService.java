package com.tsts.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class CodingStatsService {
    private static final String LOG_FILE_PATH = "/home/tayssir/projects/test-hgraph/monitor.log";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[46m";
    private static final String ANSI_BOLD = "\u001B[1m";
    public void displayCodingHours() {
        Map<LocalDate, Long> dailyCodingHours = calculateDailyCodingHours();

        if (dailyCodingHours.isEmpty()) {
            System.out.println("No coding activity found in the log.");
            return;
        }

        System.out.println("\n"+ANSI_BOLD+ ANSI_YELLOW_BACKGROUND +"--- Coding Hours Per Day ---" + ANSI_RESET);
        printTable(dailyCodingHours);

        System.out.println("\n"+ANSI_BOLD+ ANSI_YELLOW_BACKGROUND +"--- Coding Hours Curve ---" + ANSI_RESET);
        printCurveTable(dailyCodingHours);
    }

    private Map<LocalDate, Long> calculateDailyCodingHours() {
        Map<LocalDate, Long> codingHours = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (BufferedReader reader = new BufferedReader(new FileReader(CodingStatsService.LOG_FILE_PATH))) {
            String line;
            LocalDateTime startTime = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                LocalDateTime timestamp = LocalDateTime.parse(parts[0] + " " + parts[1], formatter);
                String action = parts[2];

                if (action.equals("START")) {
                    startTime = timestamp;
                } else if (action.equals("END") && startTime != null) {
                    long durationMinutes = java.time.Duration.between(startTime, timestamp).toMinutes();
                    LocalDate date = startTime.toLocalDate();
                    codingHours.put(date, codingHours.getOrDefault(date, 0L) + durationMinutes);
                    startTime = null;
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading log file: " + e.getMessage());
            e.printStackTrace();
        }
        return codingHours;
    }

    private void printTable(Map<LocalDate, Long> dailyCodingHours) {
        System.out.printf("%-12s \u001b[38;5;37m| \u001b[0m %-10s%n", "Date", "Coding Hours");
        System.out.println("-------------+-------------");
        dailyCodingHours.forEach((date, minutes) -> {
            System.out.printf("%-12s \u001B[38;5;37m| \u001b[0m %.2f \u001B[33mhours \u001B[0m%n", date, minutes / 60.0);
        });
    }

    private void printCurveTable(Map<LocalDate, Long> dailyCodingHours) {
        long maxMinutes = dailyCodingHours.values().stream().max(Long::compare).orElse(1L);

        System.out.printf("%-12s \u001b[38;5;37m| \u001b[0m %-50s \u001b[38;5;37m| \u001b[0m %-10s%n", "Date", "Activity Curve", "Hours");
        System.out.println("-------------\u001B[38;5;37m+\u001b[0m-----------------------------------------------------\u001B[38;5;37m+\u001b[0m-----------");
        dailyCodingHours.forEach((date, minutes) -> {
            int barLength = (int) ((minutes * 50) / maxMinutes); // Scale to 50 characters max
            System.out.printf("%-12s \u001B[38;5;37m| \u001b[0m %-50s \u001B[38;5;37m| \u001b[0m %.2f%n", date, "*".repeat(barLength), minutes / 60.0);
        });
    }
}
