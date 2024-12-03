package com.tsts;

import picocli.CommandLine;

@CommandLine.Command(
        name = "git-tool",
        description = "A CLI tool to analyze Git repositories",
        mixinStandardHelpOptions = true
//        subcommands = {ScanCommand.class, StatsCommand.class}
)
public class Main implements Runnable{
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
    @Override
    public void run() {
        System.out.println("Use one of the subcommands: scan or stats.");
    }
}