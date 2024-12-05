package com.tsts.commands;

import com.tsts.utils.Utils;
import picocli.CommandLine;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.util.List;

@CommandLine.Command(name = "email", description = "Analyze commits for a specific email.")
public class EmailCommand implements Runnable {

    @CommandLine.Parameters(paramLabel = "EMAIL", description = "Email address to analyze commits for")
    private String email;

    @Override
    public void run() {
        List<String> repositories = Utils.loadRepositories();
        if (repositories.isEmpty()) {
            System.out.println("No repositories found. Use the 'add' command first.");
            return;
        }

        System.out.println("Analyzing commits for email: " + email);
        for (String repoPath : repositories) {
            File repoFolder = new File(repoPath);
            try (Git git = Git.open(repoFolder)) {
                Iterable<RevCommit> commits = git.log().call();
                for (RevCommit commit : commits) {
                    if (commit.getAuthorIdent().getEmailAddress().equals(email)) {
                        System.out.println("Commit: " + commit.getFullMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to analyze repository: " + repoPath);
                e.printStackTrace();
            }
        }
    }
}

