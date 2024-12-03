package com.tsts;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "stats", description = "Display stats for a Git repository")
public class StatsCommand implements Runnable {

    @CommandLine.Parameters(index = "0", description = "The path to a Git repository")
    private File repoPath;

    @Override
    public void run() {
        try {
            Git git = Git.open(repoPath);
            Iterable<RevCommit> commits = git.log().call();

            int commitCount = 0;
            for (RevCommit commit : commits) {
                commitCount++;
            }

            System.out.println("Total commits: " + commitCount);
        } catch (Exception e) {
            System.err.println("Error reading repository: " + e.getMessage());
        }
    }
}
