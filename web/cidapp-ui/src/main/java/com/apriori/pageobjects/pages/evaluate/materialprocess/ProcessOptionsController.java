package com.apriori.pageobjects.pages.evaluate.materialprocess;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
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
    public ProcessOptionsController selectOptionsDefault(WebElement opDefaultButton) {
        pageUtils.waitForElementAndClick(opDefaultButton);
        return this;
    }

    /**
     * Inputs override value
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsController inputOptionsOverride(WebElement opOverrideButton, WebElement opOverrideInput, String value) {
        pageUtils.waitForElementAndClick(opOverrideButton);
        pageUtils.clearInput(opOverrideInput);
        opOverrideInput.sendKeys(value);
        return this;
    }

    /**
     * Selects material default
     *
     * @param label - the label
     * @return current page object
     */
    public ProcessOptionsController selectAverageWallThickness(String label) {
        By byDefaultButton = By.xpath(String.format("//h6[.='%s']/..//input[@value='default']", label));
        pageUtils.waitForElementAndClick(byDefaultButton);
        return this;
    }

    /**
     * Input material override
     *
     * @param label - the label
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsController inputAverageWallThickness(String label, String value) {
        By byOverrideButton = By.xpath(String.format("//h6[.='%s']/..//input[@value='default']", label));
        pageUtils.waitForElementAndClick(byOverrideButton);
        WebElement byOverrideInput = driver.findElement(By.xpath(String.format("//h6[.='%s']/..//input[@type='number']", label)));
        pageUtils.clearInput(byOverrideInput);
        byOverrideInput.sendKeys(value);
        return this;
    }

    /**
     * Gets material override
     *
     * @param label - the label
     * @return string
     */
    public String getMaterialOverride(String label) {
        WebElement byOverrideInput = driver.findElement(By.xpath(String.format("//h6[.='%s']/..//input[@type='number']", label)));
        return pageUtils.waitForElementToAppear(byOverrideInput).getAttribute("value");
    }

//    /**
//     * Select masking
//     *
//     * @param maskingButton - the masking button webelement
//     * @return current page object
//     */
//    public ProcessOptionsController selectMasking(WebElement maskingButton) {
//        pageUtils.waitForElementAndClick(maskingButton);
//        return this;
//    }
//
//    /**
//     * Select masked features
//     *
//     * @param maskingFeaturesButton - the masked features webelement
//     * @return current page object
//     */
//    public ProcessOptionsController selectMaskedFeatures(WebElement maskingFeaturesButton) {
//        pageUtils.waitForElementAndClick(maskingFeaturesButton);
//        return this;
//    }
//
//    /**
//     * Select masked input
//     *
//     * @param maskingInput - the masked features webelement
//     * @param value        - the value
//     * @return current page object
//     */
//    public ProcessOptionsController inputMaskedFeatures(WebElement maskingInput, String value) {
//        pageUtils.clearInput(maskingInput);
//        maskingInput.sendKeys(value);
//        return this;
//    }
//
//    /**
//     * Gets masking input
//     * @param maskingInput - the masking input
//     * @return string
//     */
//    public String getMaskedFeatures(WebElement maskingInput) {
//        return pageUtils.waitForElementToAppear(maskingInput).getAttribute("textContent");
//    }
//
//    /**
//     * Gets override input
//     *
//     * @param override - the override value
//     * @return string
//     */
//    public String getOverride(WebElement override) {
//        return pageUtils.waitForElementToAppear(override).getAttribute("textContent");
//    }
}
