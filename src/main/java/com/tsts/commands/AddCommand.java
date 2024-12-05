package com.tsts.commands;

import com.tsts.utils.Utils;
import picocli.CommandLine;
import java.io.File;
import java.util.List;

@CommandLine.Command(name = "add", description = "Scan and add a new folder for Git repositories.")
public class AddCommand implements Runnable {

    @CommandLine.Parameters(paramLabel = "FOLDER", description = "Path to the folder to scan")
    private String folderPath;

    @Override
    public void run() {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path: " + folderPath);
            return;
        }

        List<String> repositories = Utils.scanGitRepositories(folder);
        Utils.saveRepositories(repositories);
        System.out.println("Repositories added successfully.");
    }
}

