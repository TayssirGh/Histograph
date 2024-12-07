package com.tsts.presentation;

import com.tsts.services.RepoStatsService;
import picocli.CommandLine;

@CommandLine.Command(
        name = "repo-stats",
        description = "Display repository-level statistics for a specific email."
)
public class RepoStatsCommand implements Runnable {

    @CommandLine.Parameters(paramLabel = "EMAIL", description = "Email address to analyze commits for")
    private String email;

    @Override
    public void run() {
        RepoStatsService service = new RepoStatsService();
        service.displayStats(email);
    }
}
