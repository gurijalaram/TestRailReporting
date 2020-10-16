package com.apriori.utils.web.driver;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author cfrith
 */

public interface BrowserDriver {

    WebDriver startService();

    void logInfo(DesiredCapabilities dc);

    void setDownloadFolder(String downloadPath);

    void setProxy(Proxy proxy);
}
