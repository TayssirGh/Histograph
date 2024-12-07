package com.tsts.services.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
    private static final String CONFIG_FILE = System.getProperty("user.home") + "/histograph";

    public static List<String> scanGitRepositories(File folder) {
        List<String> repositories = new ArrayList<>();
        boolean hasGit = scanGitRepositoriesHelper(folder, repositories);
        if (!hasGit) {
            System.out.println("No git repositories found");
        }

        return repositories;
    }

    private static boolean scanGitRepositoriesHelper(File folder, List<String> repositories) {
        boolean foundGit = false;
        File[] files = folder.listFiles();
        if (files == null) return false;

        for (File file : files) {
            if (file.isDirectory()) {
                if (file.getName().endsWith(".git")) {
                    repositories.add(folder.getAbsolutePath());
                    return true;
                }
                foundGit |= scanGitRepositoriesHelper(file, repositories); // Recursively scan
            }
        }
        return foundGit;
    }


    public static void saveRepositories(List<String> repositories) {
        Set<String> existingRepositories = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                existingRepositories.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Failed to read existing repositories: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE, true))) {
            for (String repo : repositories) {
                if (!existingRepositories.contains(repo.trim())) {
                    writer.write(repo);
                    writer.newLine();
                    System.out.println("Repository added successfully ✅: ");
                } else {
                    System.out.println("Repository already exists: ");
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to save repositories: " + e.getMessage());
        }
    }

    public static List<String> loadRepositories() {
        List<String> repositories = new ArrayList<>();
        File file = new File(CONFIG_FILE);
        if (!file.exists()) return repositories;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                repositories.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Failed to load repositories: " + e.getMessage());
        }
        return repositories;
    }
}
