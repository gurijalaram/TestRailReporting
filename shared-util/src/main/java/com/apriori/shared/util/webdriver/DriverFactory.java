package com.apriori.shared.util.webdriver;

import static com.apriori.shared.util.testconfig.TestMode.DOCKER_GRID;
import static com.apriori.shared.util.testconfig.TestMode.HOSTED_GRID;
import static com.apriori.shared.util.testconfig.TestMode.LOCAL_GRID;
import static com.apriori.shared.util.testconfig.TestMode.QA_LOCAL;
import static com.apriori.shared.util.testconfig.TestType.API;
import static com.apriori.shared.util.testconfig.TestType.EXPORT;
import static com.apriori.shared.util.testconfig.TestType.UI;

import com.apriori.shared.util.exceptions.ArgumentNotValidException;
import com.apriori.shared.util.testconfig.Browser;
import com.apriori.shared.util.testconfig.TestMode;
import com.apriori.shared.util.testconfig.TestType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

@Slf4j
public class DriverFactory {

    @Getter
    public static TestMode testMode;
    @Getter
    public static String os = System.getProperty("os.name");
    protected static String mode = System.getProperty("mode");
    @Getter
    public Browser browser = setBrowser(System.getProperty("browser"));
    @Getter
    private WebDriver driver;
    @Getter
    public static TestType type = setTestType(System.getProperty("type"));

    public DriverFactory() {
        this.driver = createInstance();
    }

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

    public WebDriver createInstance() {

        if (mode == null || StringUtils.isEmpty(mode) || mode.equalsIgnoreCase("QA_LOCAL")) {
            testMode = QA_LOCAL;
            return driver = new LocalDriverFactory().createInstance(browser);
        }

        switch (mode.toUpperCase()) {
            case "GRID":
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
}
