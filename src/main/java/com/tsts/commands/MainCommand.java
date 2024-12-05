package com.tsts.commands;

import picocli.CommandLine;

@CommandLine.Command(
        name = "histograph",
        mixinStandardHelpOptions = true,
        description = "Git local stats utility",
        version = "histograph (1.0.0)",
        subcommands = {AddCommand.class}
)
public class MainCommand implements Runnable {
    @Override
    public void run() {
        printHGraphWithGradient();
    }
    private void printHGraphWithGradient() {
        String hgraph =
                """
                           __                      __\s
                          / /  ___ ________ ____  / /\s
                         / _ \\/ _ `/ __/ _ `/ _ \\/ _ \\
                        /_//_/\\_, /_/  \\_,_/ .__/_//_/
                             /___/        /_/         \s
                        """;

        String[] lines = hgraph.split("\n");
        String[] colors = {
                "\u001b[38;5;33m",
                "\u001b[38;5;32m",
                "\u001b[38;5;37m",
                "\u001b[38;5;39m",
                "\u001b[38;5;45m"
        };
        for (int i = 0; i < lines.length; i++) {
            System.out.println(colors[i % colors.length] + lines[i]);
        }

        System.out.print("\u001b[0m");
        System.out.println("\tversion \u001b[33m1.0.0\u001b[0m");
        System.out.println("\n" );
        System.out.println("For help, type \u001b[33mhgraph -h\u001b[0m");
        System.out.println("\n" );
        System.out.println("Welcome to \u001b[38;5;37mHistograph \u001b[0m, Let's see your activity \uD83E\uDDD0\uD83E\uDDD0" );
        System.out.println("\n" );

    }
}
