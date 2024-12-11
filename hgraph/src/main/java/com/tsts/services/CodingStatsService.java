package com.tsts.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CodingStatsService {
    private static final String LOG_FILE_PATH = "/home/tayssir/projects/test-hgraph/monitor.log";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_BOLD = "\u001B[1m";
    public void displayCodingHours() {
        Map<LocalDate, Long> dailyCodingHours = calculateDailyCodingHours();
        Map<Integer, Long> hourlyCodingFrequency = calculateHourlyCodingFrequency();

        if (dailyCodingHours.isEmpty()) {
            System.out.println("No coding activity found in the log.");
            return;
        }
        System.out.println("\n"+ANSI_BOLD+ ANSI_CYAN_BACKGROUND +"--- Coding Hours Curve ---" + ANSI_RESET);
        printCurveTable(dailyCodingHours);
        System.out.println("\n" + ANSI_BOLD + ANSI_CYAN_BACKGROUND + "--- Happy Coding Hour 🤓 ---" + ANSI_RESET);
        printHappyCodingHour(hourlyCodingFrequency);
    }
    private Map<Integer, Long> calculateHourlyCodingFrequency() {
        Map<Integer, Long> hourlyCoding = new HashMap<>();
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
                    int startHour = startTime.getHour();
                    hourlyCoding.put(startHour, hourlyCoding.getOrDefault(startHour, 0L) + durationMinutes);
                    startTime = null;
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading log file: " + e.getMessage());
            e.printStackTrace();
        }
        return hourlyCoding;
    }
    private void printHappyCodingHour(Map<Integer, Long> hourlyCoding) {
        int happyHour = 0;
        long maxMinutes = 0;

        for (Map.Entry<Integer, Long> entry : hourlyCoding.entrySet()) {
            if (entry.getValue() > maxMinutes) {
                maxMinutes = entry.getValue();
                happyHour = entry.getKey();
            }
        }

        System.out.printf("\n%-12s %-10s %-20s%n", "Hour", "Total (Minutes)", "Happy Coding Level");
        System.out.println("-------------+-----------+---------------------");

        int finalHappyHour = happyHour;
        hourlyCoding.forEach((hour, minutes) -> {
            String happyMarker = hour == finalHappyHour ? "🤓✨" : "\uD83D\uDD38";
            System.out.printf("%-12s \u001b[90m|\u001b[0m %-10d\u001b[90m| \u001b[0m%-20s%n", hour + ":00", minutes, happyMarker);
        });

        System.out.println("\n" + ANSI_BOLD  +
                "*************************************\n" +
                "*    Your Happy Coding Hour 🤓    *\n" +
                "*            \u001b[33m" + happyHour + ":00\u001b[0m             *\n" +
                "*************************************" + ANSI_RESET);
        System.out.println("\n");
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


    private void printCurveTable(Map<LocalDate, Long> dailyCodingHours) {
        long maxMinutes = dailyCodingHours.values().stream().max(Long::compare).orElse(1L);

        System.out.printf("%-12s \u001b[38;5;37m| \u001b[0m %-50s \u001b[38;5;37m| \u001b[0m %-10s%n", "Date", "Activity Curve", "Hours");
        System.out.println("-------------\u001B[38;5;37m+\u001b[0m-----------------------------------------------------\u001B[38;5;37m+\u001b[0m-----------");
        dailyCodingHours.forEach((date, minutes) -> {
            int barLength = (int) ((minutes * 50) / maxMinutes);
            System.out.printf("%-12s \u001B[38;5;37m| \u001b[0m %-50s \u001B[38;5;37m| \u001b[0m %.2f%n", date, "*".repeat(barLength), minutes / 60.0);
        });
    }
}
