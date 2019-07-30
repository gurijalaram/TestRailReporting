package main.java.utils;

import main.java.base.DriverFactory;
import main.java.base.TestMode;
import main.java.base.TestType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverUtils {

    private static WebDriver driver = new DriverFactory(TestMode.LOCAL,
            TestType.UI,
            "chrome",
            null,
            "../",
            null,
            null).getDriver();

    private static WebDriverWait wait = new WebDriverWait(driver, 10);

    public String getToken(final String email, final String password) {

        userAuthorization(email, password);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//img[@class='logo logo']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h2[text() ='Upload new Bill of Materials']")));
        return ((ChromeDriver) driver).getLocalStorage().getItem("ID_TOKEN");
    }

    public void userAuthorization(String email, String password) {
        firstUserAuthorization(email,password);
        secondUserAuthorization(email,password);
    }

    private void firstUserAuthorization(String email, String password) {
        driver.get("http://edc.qa.awsdev.apriori.com/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@name='email' and @id='email-address']"))).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@name='password' and @id='password']"))).sendKeys(password);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//button[@class='btn btn-secondary' and text()='Login']"))).click();
    }

    private void secondUserAuthorization(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@name='email' and @type='email']"))).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@name='password' and @type='password']"))).sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[@class='auth0-label-submit']"))).click();

    }
}
