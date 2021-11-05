package com.apriori.utils.web.driver;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

/**
 * @author cfrith
 */

public class ChromeDriverOptions {

    private ChromeOptions chromeOptions = new ChromeOptions();
    private HashMap<String, Object> chromePrefs = new HashMap<>();
    private boolean headless = false;
    private String downloadPath;
    private String locale;

    public ChromeDriverOptions(String downloadPath, String locale) {
        this.downloadPath = downloadPath;
        this.locale = locale;
    }

    public ChromeOptions getChromeOptions() {

        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--allow-outdated-plugins");
        // TODO: 20/10/2021 commented because this doesn't work on vnc
        //chromeOptions.setLogLevel(ChromeDriverLogLevel.OFF);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);

        // Set custom download dir
        if (downloadPath != null) {
            chromePrefs.put("download.default_directory", downloadPath);
        }
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.directory_upgrade", true);

        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-outdated-plugins");
        if (!StringUtils.isEmpty(System.getProperty("ignoreSslCheck")) && Boolean.parseBoolean(System.getProperty("ignoreSslCheck"))) {
            chromeOptions.addArguments("--ignore-certificate-errors");
        }

        // TODO: 28/02/2020 quick fix for running on linux. this will be reworked with major changes in the near future
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-dev-shm-usage");
        }

        headless = !StringUtils.isEmpty(System.getProperty("headless")) && Boolean.parseBoolean(System.getProperty("headless"));

        if (headless) {
            // note: the window size in headless is not limited to the display size
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--no-sandbox");
        }

        if (StringUtils.isNotEmpty(locale)) {
            chromeOptions.addArguments(String.format("--lang=%s", locale));
        }
        return chromeOptions;
    }
}
