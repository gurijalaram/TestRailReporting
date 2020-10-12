package com.apriori.utils.web.driver;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

/**
 * @author cfrith
 */

public class ChromeDriverOptions {

    String downloadPath;
    String locale;
    private boolean headless = false;

    public ChromeDriverOptions(String downloadPath, String locale) {
        this.downloadPath = downloadPath;
        this.locale = locale;
    }

    public ChromeOptions getChromeOptions() {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        // Set Custom Download Dir for downloads in chrome
        if (downloadPath != null) {
            chromePrefs.put("download.default_directory", downloadPath);
        }
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.directory_upgrade", true);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--allow-outdated-plugins");
        if (!StringUtils.isEmpty(System.getProperty("ignoreSslCheck")) && Boolean.parseBoolean(System.getProperty("ignoreSslCheck"))) {
            options.addArguments("--ignore-certificate-errors");
        }
        options.setExperimentalOption("prefs", chromePrefs);

        // TODO: 28/02/2020 quick fix for running on linux. this will be reworked with major changes in the near future
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            options.addArguments("--no-sandbox");
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-dev-shm-usage");
        }

        headless = !StringUtils.isEmpty(System.getProperty("headless")) && Boolean.parseBoolean(System.getProperty("headless"));

        if (headless) {
            // note: the window size in headless is not limited to the display size
            options.addArguments("headless", "disable-gpu", "window-size=1920,1080");
        }

        if (StringUtils.isNotEmpty(locale)) {
            options.addArguments("--lang=" + locale);
        }
        return options;
    }
}
