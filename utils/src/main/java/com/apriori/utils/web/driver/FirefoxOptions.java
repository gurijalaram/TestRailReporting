package com.apriori.utils.web.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxOptions {

    private DesiredCapabilities dc = new DesiredCapabilities();
    private String downloadPath;
    private String locale;

    public FirefoxOptions(String downloadPath, String locale) {
        this.downloadPath = downloadPath;
        this.locale = locale;
    }

    public DesiredCapabilities getFirefoxOptions() {
        FirefoxProfile fp = new FirefoxProfile();
        System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, WebDriverManager.firefoxdriver().getDownloadedDriverPath());
        fp.setPreference("browser.search.geoip.url", "http://127.0.0.1");
        fp.setPreference("browser.download.folderList", 2);
        fp.setPreference("browser.download.manager.showWhenStarting", false);
        fp.setPreference("browser.download.dir", downloadPath);
        fp.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, application/csv, "
            + "application/vnd.ms-powerpoint, application/ris, text/csv, image/png, application/pdf, "
            + "text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, "
            + "application/download, application/octet-stream, application/xls, application/vnd.ms-excel, "
            + "application/x-xls, application/x-ms-excel, application/msexcel, application/x-msexcel, "
            + "application/x-excel");

        if (StringUtils.isNotEmpty(locale)) {
            fp.setPreference("intl.accept_languages", locale);
        }

        dc.setCapability(FirefoxDriver.PROFILE, fp);
        return dc.merge(DesiredCapabilities.firefox());
    }
}
