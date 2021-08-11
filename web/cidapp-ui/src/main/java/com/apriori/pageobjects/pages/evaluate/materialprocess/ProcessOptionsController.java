package com.apriori.pageobjects.pages.evaluate.materialprocess;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ProcessOptionsController {

    private WebDriver driver;
    private PageUtils pageUtils;

    public ProcessOptionsController(WebDriver driver) {
        this.driver = driver;
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Selects default value radio button
     *
     * @return current page object
     */
    public ProcessOptionsController selectDefaultValue(WebElement defaultValueButton) {
        pageUtils.waitForElementAndClick(defaultValueButton);
        return this;
    }

    /**
     * Selects override radio button
     *
     * @return current page object
     */
    public ProcessOptionsController selectOverride(WebElement overrideButton) {
        pageUtils.waitForElementAndClick(overrideButton);
        return this;
    }

    /**
     * Inputs override value
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsController inputOverride(WebElement overrideInput, String value) {
        pageUtils.clearInput(overrideInput);
        overrideInput.sendKeys(value);
        return this;
    }

    /**
     * Gets override input
     * @param override - the override value
     * @return string
     */
    public String getOverride(WebElement override) {
        return pageUtils.waitForElementToAppear(override).getAttribute("textContent");
    }
}
