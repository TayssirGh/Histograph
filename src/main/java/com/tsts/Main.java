package com.tsts;

import com.tsts.commands.AddCommand;
import com.tsts.commands.EmailCommand;
import com.tsts.commands.MainCommand;
import picocli.CommandLine;

@CommandLine.Command(
        name = "hgraph",
        description = "A CLI tool to analyze Git repositories",
        mixinStandardHelpOptions = true
)
public class Main implements Runnable{
    public static void main(String[] args) {
        int exitCode = new CommandLine(new MainCommand())
//                .addSubcommand("add", new AddCommand())
                .addSubcommand("email", new EmailCommand())
                .execute(args);
        System.exit(exitCode);
    }
    @Override
    public void run() {
        System.out.println("Use one of the subcommands: scan or stats.");
    }

}