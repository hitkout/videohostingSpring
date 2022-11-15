package com.ccb.cloudbasics;

import com.ccb.cloudbasics.service.LogCollectorService;
import com.ccb.cloudbasics.service.LogCollectorServiceImpl;

import java.io.File;
import java.util.List;

public class CloudBasicsLogReader {

    public static void main(String[] args) {
        LogCollectorService lcs = new LogCollectorServiceImpl();

        String logPath = System.getenv("LOG_PATH");
        List<File> files = lcs.getAllFiles(logPath, ".log");
        System.out.println(lcs.getAllLogs(files));
    }
}
