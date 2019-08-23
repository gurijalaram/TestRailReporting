package main.java.utils;

import main.java.base.DriverFactory;
import main.java.base.TestMode;
import main.java.base.TestType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverUtils.class);

    public String getToken(final String email, final String password) {
        WebDriver driver = new DriverFactory(TestMode.LOCAL,
            TestType.UI,
            "chrome",
            null,
            "../",
            null,
            null).getDriver();

        WebDriverWait wait = new WebDriverWait(driver, 10);

        String token;

        try {
            userAuthorization(email, password, driver, wait);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//img[@class='logo logo']")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h2[text() ='Upload new Bill of Materials']")));

            token = ((ChromeDriver) driver).getLocalStorage().getItem("ID_TOKEN");
        } catch (Exception e) {
            logger.error(String.format("Can't login as valid user. User credentials: email %s, password %s", email, password));
            throw new IllegalArgumentException();
        } finally {
            driver.quit();
        }


        return token;
    }

    public void userAuthorization(String email, String password, WebDriver driver, WebDriverWait wait) {
        firstUserAuthorization(email,password, driver, wait);
        secondUserAuthorization(email,password, wait);
    }

    private void firstUserAuthorization(String email, String password, WebDriver driver, WebDriverWait wait) {
        driver.get("http://edc.qa.awsdev.apriori.com/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@name='email' and @id='email-address']"))).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@name='password' and @id='password']"))).sendKeys(password);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//button[@class='btn btn-secondary' and text()='Login']"))).click();
    }

    private void secondUserAuthorization(String email, String password, WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@name='email' and @type='email']"))).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@name='password' and @type='password']"))).sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[@class='auth0-label-submit']"))).click();

    }
}
