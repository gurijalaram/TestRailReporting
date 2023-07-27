package com.apriori;

import static com.apriori.TestMode.DOCKER_GRID;
import static com.apriori.TestMode.HOSTED_GRID;
import static com.apriori.TestMode.LOCAL_GRID;
import static com.apriori.TestMode.QA_LOCAL;
import static com.apriori.TestType.API;
import static com.apriori.TestType.EXPORT;
import static com.apriori.TestType.UI;

import com.apriori.exceptions.ArgumentNotValidException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

@Slf4j
public class DriverFactory {

    @Getter
    protected static TestMode testMode;
    protected static String mode = System.getProperty("mode");
    @Getter
    protected static String os = System.getProperty("os.name");
    @Getter
    protected Browser browser = setBrowser(System.getProperty("browser"));
    @Getter
    private WebDriver driver;

    public DriverFactory() {
        this.driver = createInstance();
    }

    public WebDriver createInstance() {

        if (mode == null || StringUtils.isEmpty(mode) || mode.equalsIgnoreCase("QA_LOCAL")) {
            testMode = QA_LOCAL;
            return driver = new LocalDriverFactory().createInstance(browser);
        }

        switch (mode.toUpperCase()) {
            case "HOSTED_GRID":
                testMode = HOSTED_GRID;
                remoteWebDriverService("conqsldocker01");
                break;
            case "LOCAL_GRID":
                testMode = LOCAL_GRID;
                remoteWebDriverService("localhost");
                break;
            case "DOCKER_GRID":
                testMode = DOCKER_GRID;
                remoteWebDriverService("host.docker.internal");
                break;
            default:
                throw new ArgumentNotValidException(mode);
        }
        return driver;
    }

    private void remoteWebDriverService(String host) {
        driver = new RemoteDriverFactory().createInstance(this.browser, ("http://").concat(host).concat(":4444"));
    }

    @Getter
    protected static TestType type = setTestType(System.getProperty("type"));

    private static Browser setBrowser(String browser) {
        return browser == null || browser.isEmpty() ? Browser.CHROME : Browser.valueOf(browser.toUpperCase());
    }

    private static TestType setTestType(String testType) {
        if (testType == null || StringUtils.isEmpty(testType) || testType.equalsIgnoreCase("UI")) {
            return UI;
        }
        switch (testType.toUpperCase()) {
            case "UI":
                type = UI;
                break;
            case "EXPORT":
                type = EXPORT;
                break;
            case "API":
                type = API;
                break;
            default:
                throw new ArgumentNotValidException(testType);
        }
        return type;
    }
}
