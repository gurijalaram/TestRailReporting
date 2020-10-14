package com.apriori.utils.web.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public interface BrowserDriver {

    void createService();
    WebDriver startService();
    void stopService();
    void logInfo(DesiredCapabilities dc);
}
