package com.apriori.utils.web.driver;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FirefoxDriverOptions {

    private FirefoxOptions firefoxOptions = new FirefoxOptions();
    private FirefoxProfile firefoxProfile = new FirefoxProfile();
    private String downloadPath;
    private String locale;

    public FirefoxDriverOptions(String downloadPath, String locale) {
        this.downloadPath = downloadPath;
        this.locale = locale;
    }

    public FirefoxOptions getFirefoxOptions() {
        firefoxOptions.setAcceptInsecureCerts(true);
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.INFO);

        // Set custom download dir
        if (downloadPath == null) {
            firefoxProfile.setPreference("browser.download.folderList", 1);
        } else {
            firefoxProfile.setPreference("browser.download.folderList", 2);
            firefoxProfile.setPreference("browser.download.dir", downloadPath);
        }

        firefoxProfile.setPreference("browser.search.geoip.url", "http://127.0.0.1");

        firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, application/csv, "
            + "application/vnd.ms-powerpoint, application/ris, text/csv, image/png, application/pdf, "
            + "text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, "
            + "application/download, application/octet-stream, application/xls, application/vnd.ms-excel, "
            + "application/x-xls, application/x-ms-excel, application/msexcel, application/x-msexcel, "
            + "application/x-excel");

        if (StringUtils.isNotEmpty(locale)) {
            firefoxProfile.setPreference("intl.accept_languages", locale);
        }

        return firefoxOptions.setProfile(firefoxProfile);
    }
}
