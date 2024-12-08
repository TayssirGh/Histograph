package com.tsts.presentation;

import com.tsts.services.AddService;
import picocli.CommandLine;

@CommandLine.Command(name = "add", description = "Scan and add a new folder for Git repositories.")
public class AddCommand implements Runnable {

    @CommandLine.Parameters(paramLabel = "FOLDER", description = "Path to the folder to scan")
    private String folderPath;

    @Override
    public void run() {
        AddService addService = new AddService();
        addService.add(folderPath);
    }


}

