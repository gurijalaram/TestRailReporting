package com.apriori.utils.web.util;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * @author kpatel
 */
public class ConsoleLogHandler implements WebDriverEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleLogHandler.class);
    private Level levelOfThrowingAssertion;
    private List<String> blacklistedURLs = Arrays.asList(
        "pbs.twimg.com",
        "doubleclick.net",
        "chrome-extension://invalid/",
        "bootstrap.min.js",
        "secure.gravatar.com",
        "cid-te.awsdev.apriori.com/apriori/cost/session/ws/userbean"
    );

    /**
     * @param levelOfThrowingAssertion choose one of four levels of throwing assertion:
     *                                 Level.OFF - every browser console log will be allowed
     *                                 Level.SEVERE - throws new {@link ConsoleLogError} if ERROR appears on browser console
     *                                 Level.WARNING - every browser console log will be allowed
     *                                 Level.INFO - every browser console log will be allowed
     */
    public ConsoleLogHandler(Level levelOfThrowingAssertion) {
        this.levelOfThrowingAssertion = levelOfThrowingAssertion;
    }

    private List<LogEntry> filterBlacklistedURLs(List<LogEntry> nonFilteredEntries) {
        List<LogEntry> filtered = new ArrayList<>();
        nonFilteredEntries.forEach(logEntry -> {
            boolean matchedWithBlackListed = blacklistedURLs.stream().anyMatch(blackListedUrl -> logEntry.getMessage().contains(blackListedUrl));
            if (!matchedWithBlackListed) {
                filtered.add(logEntry);
            }
        });
        return filtered;
    }

    private void checkBrowserConsoleLogForErrors(WebDriver driver) {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);

        List<LogEntry> filteredErrorEntries = filterBlacklistedURLs(logEntries.getAll());
        if (!filteredErrorEntries.isEmpty() && levelOfThrowingAssertion.equals(Level.SEVERE)) {
            filteredErrorEntries.forEach(logEntry -> {
                throw new ConsoleLogError("Browser console ERROR: " + logEntry.getMessage());
            });
        }
        if (!filteredErrorEntries.isEmpty() && !levelOfThrowingAssertion.equals(Level.SEVERE) && !levelOfThrowingAssertion.equals(Level.OFF)) {
            filteredErrorEntries.forEach(logEntry -> {
                logger.debug("Browser console ERROR: " + logEntry.getMessage());
            });
        }
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        checkBrowserConsoleLogForErrors(webDriver);
    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        checkBrowserConsoleLogForErrors(webDriver);
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeSwitchToWindow(String s, WebDriver webDriver) {

    }

    @Override
    public void afterSwitchToWindow(String s, WebDriver webDriver) {

    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> outputType) {

    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> outputType, X x) {

    }

    @Override
    public void beforeGetText(WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void afterGetText(WebElement webElement, WebDriver webDriver, String s) {

    }

    @Override
    public void afterAlertAccept(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);

    }

    @Override
    public void afterAlertDismiss(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);

    }

    @Override
    public void beforeAlertAccept(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);

    }

    @Override
    public void beforeAlertDismiss(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);

    }

}
