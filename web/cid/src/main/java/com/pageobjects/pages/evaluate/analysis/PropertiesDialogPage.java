package com.pageobjects.pages.evaluate.analysis;

import com.apriori.utils.PageUtils;

import com.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesDialogPage extends LoadableComponent<PropertiesDialogPage> {

    private final Logger logger = LoggerFactory.getLogger(PropertiesDialogPage.class);

    @FindBy(id = "propertiesPanel")
    private WebElement propertyPanel;

    @FindBy(css = "div[id='propertiesPanel'] .glyphicon.glyphicon-remove")
    private WebElement closeButton;

    @FindBy(css = "div[data-ap-comp='gcdProperties'] .v-grid-scroller")
    private WebElement verticalScroller;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PropertiesDialogPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(propertyPanel);
    }

    /**
     * Closes the properties box
     *
     * @return current page object
     */
    public PropertiesDialogPage closeProperties() {
        pageUtils.waitForElementAndClick(closeButton);
        return this;
    }

    /**
     * Closes the dropdown
     *
     * @param dropdown - the dropdown to be selected
     * @return current page object
     */
    public PropertiesDialogPage minimizeDropdown(String dropdown) {
        By caret = By.xpath("//div[.='" + dropdown + "']//span[@class]");
        if (driver.findElement(caret).getAttribute("outerHTML").contains("down")) {
            driver.findElement(caret).click();
        }
        return this;
    }

    /**
     * Opens the dropdown
     *
     * @param dropdown - the dropdown to be selected
     * @return current page object
     */
    public PropertiesDialogPage expandDropdown(String dropdown) {
        By caret = By.xpath("//div[.='" + dropdown + "']//span[@class]");
        pageUtils.waitForElementToAppear(caret);

        if (driver.findElement(caret).getAttribute("outerHTML").contains("down")) {
            return this;
        } else {
            if (driver.findElement(caret).getAttribute("outerHTML").contains("right")) {
                driver.findElement(caret).click();
            }
        }
        return this;
    }

    /**
     * Check the GCD name is displayed
     *
     * @param gcdName - the gcd name
     * @return true/false
     */
    public boolean isDisplayedGCDName(String gcdName) {
        By gcd = By.xpath("//div[@data-ap-comp='gcdProperties']//div[.='" + gcdName + "']");
        return driver.findElement(gcd).isDisplayed();
    }

    /**
     * Gets the properties
     * The fully qualified name of the property must be entered as the locator looks for an exact match eg. "Finished Area (mm^2)"
     *
     * @param properties
     * @return string
     */
    public String getProperties(String properties) {
        By propertiesInfo = By.xpath("//tr[.='" + properties + "']/ancestor::tr");
        pageUtils.scrollToElement(propertiesInfo, verticalScroller, Constants.ARROW_DOWN);
        return driver.findElement(propertiesInfo).getText();
    }
}