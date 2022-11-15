package com.ccb.cloudbasics.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogCollectorServiceImpl implements com.ccb.cloudbasics.service.LogCollectorService {
    public List<File> getAllFiles(String path, String extension) {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            return paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(document -> document.getName().endsWith(extension))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public String getAllLogs(List<File> filesFromDirectory) {
        List<String> logs = new ArrayList<>();

        if (filesFromDirectory.isEmpty()) {
            return "No log files in this directory (and it's subdirectories).";
        }

        for (File file : filesFromDirectory) {
            try {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    logs.add(reader.nextLine());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return String.join("\n", logs);
    }
}
