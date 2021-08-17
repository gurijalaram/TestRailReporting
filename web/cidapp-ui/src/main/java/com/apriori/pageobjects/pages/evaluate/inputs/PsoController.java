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
     * @param userValue - the value to build the xpath
     * @return webelement
     */
    public WebElement userXpath(String userValue) {
        return pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//h6[contains(text(),'%s')]/..//input[@value='user']", userValue))));
    }

    /**
     * Builds user input xpath
     *
     * @param userValue - the value to build the xpath
     * @return webelement
     */
    public WebElement inputXpath(String userValue) {
        return pageUtils.waitForElementToAppear(driver.findElement(By.xpath(String.format("//h6[contains(text(),'%s')]/..//input[@value='number']", userValue))));
    }
}
