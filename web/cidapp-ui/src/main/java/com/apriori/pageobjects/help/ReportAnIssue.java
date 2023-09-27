package com.apriori.pageobjects.help;

import com.apriori.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ReportAnIssue extends LoadableComponent<ReportAnIssue> {

    @FindBy(css = ".MuiPaper-root h2")
    private WebElement header;

    @FindBy(css = ".close-modal")
    private WebElement close;

    @FindBy(xpath = "//button[.='Copy To Clipboard']")
    private WebElement copyToClipboard;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ReportAnIssue(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(header);
        pageUtils.waitForElementToAppear(copyToClipboard);
    }

    /**
     * Return the given value for a specified field
     *
     * @param fieldName - String used for field label
     * @return - String of value for the specified field
     */
    public String getFieldValue(String fieldName) {
        By locator = By.xpath(String.format("//span[.='%s']/following-sibling::span", fieldName));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Close the Report an Issue modal
     *
     * @param klass - The intended PO to be returned to
     * @return - New PO specified in klass
     */
    public <T> T close(Class<T> klass) {
        pageUtils.waitForElementAndClick(close);
        return PageFactory.initElements(driver, klass);
    }
}
