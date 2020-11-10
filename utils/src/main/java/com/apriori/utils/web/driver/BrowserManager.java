package com.apriori.utils.web.driver;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.logging.Level;

/**
 * @author cfrith
 */

public abstract class BrowserManager implements BrowserDriver {

    private DesiredCapabilities dc;

    @Override
    public void logInfo(DesiredCapabilities dc) {
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        logs.enable(LogType.PERFORMANCE, Level.ALL);
        dc.setCapability(CapabilityType.LOGGING_PREFS, logs);
    }

    @Override
    public void setDownloadFolder(String downloadPath) {
        File downloadDir = new File(downloadPath);
        if (!downloadDir.exists()) {
            downloadDir.mkdir();
        }
    }

    @Override
    public void setProxy(Proxy proxy) {
        if (proxy != null) {
            dc.setCapability(CapabilityType.PROXY, proxy);
        }
    }
}
