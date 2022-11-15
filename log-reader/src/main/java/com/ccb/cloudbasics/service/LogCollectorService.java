package com.ccb.cloudbasics.service;

import java.io.File;
import java.util.List;

public interface LogCollectorService {
    List<File> getAllFiles(String path, String extension);
    String getAllLogs(List<File> filesFromDirectory);
}
