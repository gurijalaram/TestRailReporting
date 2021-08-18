package com.apriori.pageobjects.pages.evaluate.inputs;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;


@Slf4j
public class PsoController {

    private WebDriver driver;
    private PageUtils pageUtils;

    public PsoController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Input override value
     *
     * @param value    - the value
     * @param override - the override radio button
     * @param input    - input locator
     * @return current page object
     */
    public PsoController inputOverrideValue(WebElement override, WebElement input, String value) {
        pageUtils.waitForElementAndClick(override);
        pageUtils.clear(input);
        input.sendKeys(value);
        return this;
    }

    /**
     * Builds user override xpath
     *
     * @param locatorValue - the value to build the xpath
     * @return webelement
     */
    public WebElement userLocator(String locatorValue) {
        return pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//h6[.='%s']/..//input[@value='user']", locatorValue))));
    }

    /**
     * Builds user input xpath
     *
     * @param locatorValue - the value to build the xpath
     * @return webelement
     */
    public WebElement inputLocator(String locatorValue) {
        return pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//h6[.='%s']/..//input[@value='number']", locatorValue))));
    }

    /**
     * Builds user override xpath
     *
     * @param locatorValue - the value to build the xpath
     * @return webelement
     */
    public WebElement defaultLocator(String locatorValue) {
        return pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//h6[.='%s']/..//input[@value='default']", locatorValue))));
    }

    /**
     * Builds user input xpath
     *
     * @param locatorValue - the value to build the xpath
     * @return webelement
     */
    public WebElement definedModeLocator(String locatorValue) {
        return pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//h6[.='%s']/..//input[@value='userDefinedMode']", locatorValue))));
    }

    /**
     * Builds user input xpath
     *
     * @param locatorValue - the value to build the xpath
     * @return webelement
     */
    public WebElement definedLocator(String locatorValue) {
        return pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//h6[.='%s']/..//input[@value='userDefined']", locatorValue))));
    }

    /**
     * Builds user override xpath
     *
     * @param locatorValue - the value to build the xpath
     * @return webelement
     */
    public WebElement overrideLocator(String locatorValue) {
        return pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//h6[.='%s']/..//input[@value='userOverride']", locatorValue))));
    }

    /**
     * Select options dropdown
     *
     * @param dropdown - the dropdown
     * @return webelement
     */
    public WebElement dropdownLocator(String dropdown) {
        return driver.findElement(By.xpath(String.format("//label[.='%s']/..//div[@class='apriori-select searchable css-1ilwabi-container']", dropdown)));
    }

    /**
     * Gets value for overridden PSO
     * @param pso - the pso
     * @return double
     */
    public double getOverriddenPso(String pso) {
        return Double.parseDouble(inputLocator(pso).getAttribute("value"));
    }

}
