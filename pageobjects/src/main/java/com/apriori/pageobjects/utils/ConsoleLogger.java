package com.apriori.pageobjects.utils;

import org.junit.runners.model.FrameworkMethod;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author kpatel
 */
public class ConsoleLogger {

    public static final Logger webdriver_logger = LoggerFactory.getLogger("webdriver");

    public static void printConsoleEntries(Logs logs, FrameworkMethod frameworkMethod) {
        LogEntries logEntries = logs.get(LogType.BROWSER);
        logEntries = filterLogEntries(logEntries);

        if (logEntries != null && !logEntries.getAll().isEmpty()) {
            MDC.put("methodName", frameworkMethod.getMethod().getDeclaringClass().getSimpleName() + "." + frameworkMethod.getName() + "-console");
            ConsoleLogger.webdriver_logger.debug("Console errors from browser:");
            for (LogEntry logEntry : logEntries) {
                ConsoleLogger.webdriver_logger.debug(logEntry.getMessage());
            }
            ConsoleLogger.webdriver_logger.debug("----------------------------------");
            MDC.remove("methodName");
        }
    }

    public static void print(String log, FrameworkMethod frameworkMethod) {
        MDC.put("methodName", frameworkMethod.getMethod().getDeclaringClass().getSimpleName() + "." + frameworkMethod.getName() + "-console");
        ConsoleLogger.webdriver_logger.debug(log);
        MDC.remove("methodName");
    }

    private static LogEntries filterLogEntries(LogEntries logEntries) {
        ArrayList<LogEntry> newLogEntryList = logEntries.getAll().stream().filter(logEntry -> !logEntry.getMessage().contains("engine-wasm.js")).collect(Collectors.toCollection(ArrayList::new));

        return new LogEntries(newLogEntryList);
    }
}
