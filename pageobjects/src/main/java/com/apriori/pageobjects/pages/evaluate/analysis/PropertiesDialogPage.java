package com.apriori.pageobjects.pages.evaluate.analysis;

import com.apriori.pageobjects.utils.PageUtils;

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
     * @param dropdown - the dropdown to be selected
     * @return current page object
     */
    public PropertiesDialogPage closeDropdown(String dropdown) {
        WebElement selection = driver.findElement(By.xpath("//div[.='" + dropdown + "']//span[@class]"));
        if (selection.getAttribute("outerHTML").contains("down")) {
            selection.click();
        }
        return this;
    }

    /**
     * Opens the dropdown
     * @param dropdown - the dropdown to be selected
     * @return current page object
     */
    public PropertiesDialogPage openDropdown(String dropdown) {
        WebElement selection = driver.findElement(By.xpath("//div[.='" + dropdown + "']//span[@class]"));
        if (selection.getAttribute("outerHTML").contains("right")) {
            selection.click();
        }
        return this;
    }

    /**
     * Check the GCD name is displayed
     * @param GCDName - the gcd name
     * @return true/false
     */
    public boolean isDisplayedGCDName(String GCDName) {
        WebElement gcd = driver.findElement(By.xpath("//div[@data-ap-comp='gcdProperties']//div[.='" + GCDName + "']"));
        return gcd.isDisplayed();
    }

    /** Gets the properties
     * The full qualified name of the property must be entered as the locator looks for an exact match eg. "Finished Area (mm^2)"
     * @param properties
     * @return string
     */
    public String getProperties(String properties) {
        By propertiesInfo = By.xpath("//tr[.='" + properties + "']/ancestor::tr");
        pageUtils.scrollToElement(propertiesInfo, verticalScroller);
        return driver.findElement(propertiesInfo).getText();
    }
}
