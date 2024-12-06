package com.tsts.commands;

import com.tsts.utils.Utils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import picocli.CommandLine;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

@CommandLine.Command(name = "email", description = "Analyze commits for a specific email.")
public class EmailCommand implements Runnable {

    @CommandLine.Parameters(paramLabel = "EMAIL", description = "Email address to analyze commits for")
    private String email;

    private static final int DAYS_IN_LAST_SIX_MONTHS = 183;

    @Override
    public void run() {
        List<String> repositories = Utils.loadRepositories();
        if (repositories.isEmpty()) {
            System.out.println("No repositories found. Use the 'add' command first.");
            return;
        }

        Map<LocalDate, Integer> commitCounts = new HashMap<>();

        System.out.println("Analyzing commits for email: " + email);
        for (String repoPath : repositories) {
            File repoFolder = new File(repoPath);
            try (Git git = Git.open(repoFolder)) {
                Iterable<RevCommit> commits = git.log().call();
                for (RevCommit commit : commits) {
                    if (commit.getAuthorIdent().getEmailAddress().equals(email)) {
                        LocalDate commitDate = commit.getAuthorIdent().getWhen()
                                .toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate();

                        if (isWithinLastSixMonths(commitDate)) {
                            commitCounts.put(commitDate, commitCounts.getOrDefault(commitDate, 0) + 1);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to analyze repository: " + repoPath);
                e.printStackTrace();
            }
        }

        printGraph(commitCounts);
    }

    private boolean isWithinLastSixMonths(LocalDate date) {
        LocalDate sixMonthsAgo = LocalDate.now().minusDays(DAYS_IN_LAST_SIX_MONTHS);
        return !date.isBefore(sixMonthsAgo) && !date.isAfter(LocalDate.now());
    }

    private void printGraph(Map<LocalDate, Integer> commitCounts) {
        LocalDate startDate = LocalDate.now().minusDays(DAYS_IN_LAST_SIX_MONTHS);
        LocalDate endDate = LocalDate.now();

        Map<String, List<List<Integer>>> monthWeeksData = new LinkedHashMap<>();

        LocalDate currentDate = startDate;
        String currentMonth = currentDate.getMonth().toString().substring(0, 3);
        List<Integer> currentWeek = new ArrayList<>(Collections.nCopies(7, 0));
        List<List<Integer>> weeksInMonth = new ArrayList<>();

        while (!currentDate.isAfter(endDate)) {
            int dayOfWeek = currentDate.getDayOfWeek().getValue() % 7;
            int commits = commitCounts.getOrDefault(currentDate, 0);

            currentWeek.set(dayOfWeek, commits);

            if (dayOfWeek == 6 || currentDate.equals(endDate)) {
                weeksInMonth.add(new ArrayList<>(currentWeek));
                currentWeek = new ArrayList<>(Collections.nCopies(7, 0));
            }

            String newMonth = currentDate.plusDays(1).getMonth().toString().substring(0, 3);
            if (!currentMonth.equals(newMonth)) {
                monthWeeksData.put(currentMonth, new ArrayList<>(weeksInMonth));
                weeksInMonth.clear();
                currentMonth = newMonth;
            }

            currentDate = currentDate.plusDays(1);
        }

        if (!weeksInMonth.isEmpty()) {
            monthWeeksData.put(currentMonth, weeksInMonth);
        }

        printHeader(monthWeeksData);
        printDaysAndData(monthWeeksData);
    }

    private void printHeader(Map<String, List<List<Integer>>> monthWeeksData) {
        System.out.print("    ");
        for (String month : monthWeeksData.keySet()) {
            int numWeeks = monthWeeksData.get(month).size();
            System.out.printf("%" + (numWeeks * 4) + "s", month);
        }
        System.out.println();
    }

    private void printDaysAndData(Map<String, List<List<Integer>>> monthWeeksData) {
        String[] days = {"Sun", " ", " ", "Wed", " ", " ", "Sat"};
        for (int i = 0; i < 7; i++) {
            System.out.printf("%3s ", days[i]);
            for (List<List<Integer>> monthWeeks : monthWeeksData.values()) {
                for (List<Integer> week : monthWeeks) {
                    System.out.printf("%s", formatCell(week.get(i)));
                }
            }
            System.out.println();
        }
    }

    private String formatCell(int commits) {
        String escape = "\033[0;37m"; // Default color
         if (commits > 0 && commits < 5) {
            escape = "\033[1;30;47m"; // Light gray
        } else if (commits >= 5 && commits < 10) {
            escape = "\033[1;30;43m"; // Yellow
        } else if (commits >= 10) {
            escape = "\033[1;30;42m"; // Green
        }

        return String.format(escape + " %2d " + "\033[0m", commits);
    }
}
