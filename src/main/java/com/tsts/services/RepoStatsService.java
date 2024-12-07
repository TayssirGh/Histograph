package com.tsts.services;

import com.tsts.services.utils.Utils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.time.Instant;
import java.util.*;

public class RepoStatsService {

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
        System.out.println("\n--- Top Repositories ---");
        repoCommitCounts.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .forEach(entry -> System.out.printf("Repository: %s, Commits: %d%n", entry.getKey(), entry.getValue()));
    }

    private void printActivityDistribution(Map<String, Integer> repoCommitCounts) {
        System.out.println("\n--- Repository Activity Distribution ---");
        int totalCommits = repoCommitCounts.values().stream().mapToInt(Integer::intValue).sum();
        repoCommitCounts.forEach((repo, count) -> {
            double percentage = (count / (double) totalCommits) * 100;
            System.out.printf("Repository: %s, Contribution: %.2f%%%n", repo, percentage);
        });
    }

    private void printLastActiveRepository(Map<String, Instant> lastCommitTimes) {
        System.out.println("\n--- Last Active Repository ---");
        lastCommitTimes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> System.out.printf("Repository: %s, Last Commit Time: %s%n", entry.getKey(), entry.getValue()));
    }
}
