package com.tsts.services;

import com.tsts.services.utils.Utils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.time.Instant;
import java.util.*;

public class RepoStatsService {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public void displayStats(String email) {
        List<String> repositories = Utils.loadRepositories();
        if (repositories.isEmpty()) {
            System.out.println("No repositories found. Use the 'add' command first.");
            return;
        }

        Map<String, Integer> repoCommitCounts = new HashMap<>();
        Map<String, Instant> lastCommitTimes = new HashMap<>();

        System.out.println("Analyzing commits for email: " + email);
        for (String repoPath : repositories) {
            File repoFolder = new File(repoPath);
            try (Git git = Git.open(repoFolder)) {
                Iterable<RevCommit> commits = git.log().call();
                for (RevCommit commit : commits) {
                    if (commit.getAuthorIdent().getEmailAddress().equals(email)) {
                        repoCommitCounts.put(repoPath, repoCommitCounts.getOrDefault(repoPath, 0) + 1);

                        Instant commitTime = commit.getAuthorIdent().getWhen().toInstant();
                        lastCommitTimes.put(repoPath, commitTime);
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to analyze repository: " + repoPath);
                e.printStackTrace();
            }
        }

        if (repoCommitCounts.isEmpty()) {
            System.out.println("No commits found for the provided email.");
            return;
        }

        printTopRepositories(repoCommitCounts);
        printActivityDistribution(repoCommitCounts);
        printLastActiveRepository(lastCommitTimes);
    }

    private void printTopRepositories(Map<String, Integer> repoCommitCounts) {
        System.out.println(ANSI_BOLD + ANSI_YELLOW_BACKGROUND +"\n--- Top Repositories \uD83C\uDFC6 ---"+ ANSI_RESET);
        repoCommitCounts.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .forEach(entry -> {
                    String repoName = new File(entry.getKey()).getName();
                    System.out.printf("%-20s \u001b[38;5;37m->\u001b[0m %d%n", repoName, entry.getValue());
                });
    }

    private void printActivityDistribution(Map<String, Integer> repoCommitCounts) {
        System.out.println(ANSI_BOLD + ANSI_YELLOW_BACKGROUND +
                "\n--- Repository Activity Distribution \uD83D\uDCCA ---"+ ANSI_RESET);
        int totalCommits = repoCommitCounts.values().stream().mapToInt(Integer::intValue).sum();
        repoCommitCounts.forEach((repo, count) -> {
            String repoName = new File(repo).getName();
            double percentage = (count / (double) totalCommits) * 100;
            String bar = generateBar(percentage);
            System.out.printf("%-30s | %-6.2f \u001B[38;5;37m%%\u001B[0m | \u001b[38;5;37m%s%n\u001b[0m", repoName, percentage, bar);
        });
    }

    private String generateBar(double percentage) {
        int barLength = (int) (percentage / 2);
        return "\uD83D\uDFE6".repeat(barLength) ;
    }

    private void printLastActiveRepository(Map<String, Instant> lastCommitTimes) {
        System.out.println(ANSI_BOLD + ANSI_YELLOW_BACKGROUND +"\n--- Last Active Repository \uD83D\uDD0E ---"+ ANSI_RESET);
        lastCommitTimes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> {
                    String repoName = new File(entry.getKey()).getName();
                    System.out.printf("%s : %s%n", repoName, entry.getValue());
                    System.out.println("\n");
                });
    }
}
