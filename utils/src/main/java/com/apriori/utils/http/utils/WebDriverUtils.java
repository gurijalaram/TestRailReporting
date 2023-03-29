package com.apriori.utils.http.utils;

import com.apriori.utils.web.driver.BrowserTypes;
import com.apriori.utils.web.driver.DriverFactory;
import com.apriori.utils.web.driver.TestMode;
import com.apriori.utils.web.driver.TestType;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverUtils.class);


    private static final String xpathAprioriLogo = ".//img[@class='logo']";
    private static final String xpathAprioriHomeText = ".//div[text() ='Uploaded Bill of Materials']";

    private static final String xpathFirstFormEmail = ".//input[@name='email' and @id='email-address']";
    private static final String xpathFirstFormPassword = ".//input[@name='password' and @id='password']";
    private static final String xpathFirstFormLoginButton = ".//button[@class='btn btn-secondary' and text()='Login']";

    private static final String xpathSecondFormEmail = ".//input[@name='email' and @type='email']";
    private static final String xpathSecondFormPassword = ".//input[@name='password' and @type='password']";
    private static final String xpathSecondFormLoginButton = ".//span[@class='auth0-label-submit']";

    public String getToken(final String email, final String password) {
        WebDriver driver = new DriverFactory(TestMode.QA_LOCAL,
            TestType.UI,
            BrowserTypes.CHROME,
            null,
            "../",
            null,
            null).getDriver();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        String token;

        try {
            userAuthorization(email, password, driver, wait, false);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathAprioriLogo)));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathAprioriHomeText)));

            token = ((ChromeDriver) driver).getLocalStorage().getItem("ID_TOKEN");
        } catch (Exception e) {
            logger.error(String.format("Can't login as valid user. User credentials: email %s, password %s", email, password));
            throw new IllegalArgumentException(e);
        } finally {
            driver.quit();
        }

        return token;
    }

    public void userAuthorization(String email, String password, WebDriver driver, WebDriverWait wait, boolean twoFormAuth) {
        driver.get("http://edc.qa.awsdev.apriori.com/login");

        if (twoFormAuth) {
            firstUserAuthorization(email, password, driver, wait);
        }

        secondUserAuthorization(email, password, wait);
    }

    private void firstUserAuthorization(String email, String password, WebDriver driver, WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathFirstFormEmail))).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathFirstFormPassword))).sendKeys(password);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathFirstFormLoginButton))).click();
    }

    private void secondUserAuthorization(String email, String password, WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathSecondFormEmail))).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathSecondFormPassword))).sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathSecondFormLoginButton))).click();

    }
}
