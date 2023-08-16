package com.apriori.pageobjects.evaluate.inputs;

import com.apriori.PageUtils;

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
        pageUtils.waitForElementToBeClickable(override);
        pageUtils.javaScriptClick(override);
        pageUtils.clearValueOfElement(input);
        input.sendKeys(value);
        return this;
    }

    /**
     * Builds user input xpath
     *
     * @param locatorValue - the value to build the xpath
     * @return webelement
     */
    public WebElement inputLocator(String locatorValue) {
        By byLocator = By.xpath(String.format("//div[text()='%s']/../..//input[@type='number']", locatorValue));
        pageUtils.waitForElementToAppear(byLocator);
        return pageUtils.scrollWithJavaScript(driver.findElement(byLocator), true);
    }

    /**
     * Builds xpath locator for element
     *
     * @param label - the label to build the xpath
     * @param value - the attribute value to build the xpath
     * @return webelement
     */
    public WebElement buildLocator(String label, String value) {
        By byLocator = By.xpath(String.format("//div[text()='%s']/../..//input[@value='%s']", label, value));
        return pageUtils.waitForElementToAppear(byLocator);
    }

    /**
     * Select options dropdown
     *
     * @param dropdown - the dropdown
     * @return webelement
     */
    public WebElement dropdownLocator(String dropdown) {
        By byLocator = By.xpath(String.format("//label[text()='%s']/..", dropdown));
        return pageUtils.waitForElementToAppear(byLocator).findElement(By.cssSelector(".apriori-select"));
    }

    /**
     * Gets value for overridden PSO
     *
     * @param pso - the pso
     * @return double
     */
    public double getOverriddenPso(String pso) {

        final String value = inputLocator(pso).getAttribute("value");

        if (value.isEmpty()) {
            return -0.0;
        } else {
            return Double.parseDouble(value);
        }
    }
}
