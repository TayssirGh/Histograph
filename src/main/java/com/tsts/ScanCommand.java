package com.tsts;


import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

@CommandLine.Command(name = "scan", description = "Scan a folder for Git repositories")
public class ScanCommand implements Runnable {

    @CommandLine.Parameters(index = "0", description = "The folder to scan")
    private Path folder;

    @Override
    public void run() {
        System.out.println("Scanning folder: " + folder);

        File[] files = folder.toFile().listFiles();
        if (files == null) {
            System.err.println("Invalid folder.");
            return;
        }

        for (File file : files) {
            if (new File(file, ".git").exists()) {
                System.out.println("Found Git repository: " + file.getAbsolutePath());
            }
        }
    }
}