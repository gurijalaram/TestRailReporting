package com.apriori.siteLicenses;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class LicenseErrorPage extends LoadableComponent<LicenseErrorPage> {

    @FindBy(className = "Toastify__toast-container")
    private WebElement errorMessageContainer;

    @FindBy(css = "[class='Toastify__toast-body']")
    private WebElement toastify;

    private WebDriver driver;
    private PageUtils pageUtils;

    public LicenseErrorPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(errorMessageContainer);
    }

    /**
     * Gets error message
     *
     * @return string error message
     */
    public String getErrorMessage() {
        String message = pageUtils.waitForElementToAppear(toastify).getAttribute("textContent");
        pageUtils.waitForElementsToNotAppear(By.cssSelector("[class='Toastify__toast-body']"));
        pageUtils.clearInput(driver.findElement(By.cssSelector("input[type='file']")));
        return message;
    }
}
