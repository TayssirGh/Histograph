package com.tsts;

import com.tsts.presentation.MainCommand;
import picocli.CommandLine;

@CommandLine.Command(
        name = "hgraph",
        description = "A CLI tool to analyze Git repositories",
        mixinStandardHelpOptions = true
)
public class Main implements Runnable{
    public static void main(String[] args) {
        int exitCode = new CommandLine(new MainCommand())
                .execute(args);
        System.exit(exitCode);
    }
    @Override
    public void run() {
        System.out.println("Use one of the subcommands: scan or stats.");
    }

}