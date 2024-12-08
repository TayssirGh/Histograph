package com.tsts.services;

import com.tsts.services.utils.Utils;

import java.io.File;
import java.util.List;

public class AddService {
    public void add(String folderPath){
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path: " + folderPath);
            return;
        }

        List<String> repositories = Utils.scanGitRepositories(folder);
        if (!repositories.isEmpty()) {
            Utils.saveRepositories(repositories);
        }
    }
}
