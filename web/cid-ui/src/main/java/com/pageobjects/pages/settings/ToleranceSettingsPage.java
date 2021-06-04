package com.pageobjects.pages.settings;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ToleranceSettingsPage extends LoadableComponent<ToleranceSettingsPage> {

    private static final Logger logger = LoggerFactory.getLogger(ToleranceSettingsPage.class);

    @FindBy(css = "input[data-ap-comp='SYSTEMDEFAULT']")
    private WebElement assumeDefaultCheckbox;

    @FindBy(css = "input[data-ap-comp='PARTOVERRIDE']")
    private WebElement specificCheckbox;

    @FindBy(css = "button[data-ap-nav-dialog='showPartOverrideTolerances']")
    private WebElement editValuesButton;

    @FindBy(css = "input[data-ap-comp='CAD']")
    private WebElement cadModelCheckbox;

    @FindBy(css = "input[data-ap-field='useCadToleranceThreshhold']")
    private WebElement replaceValuesCheckbox;

    @FindBy(css = "input[data-ap-field='minCadToleranceThreshhold']")
    private WebElement minCADInput;

    @FindBy(css = "input[data-ap-field='cadToleranceReplacement']")
    private WebElement cadReplacementInput;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ToleranceSettingsPage(WebDriver driver) {
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
     * Selects assume tolerance checkbox
     *
     * @return current page object
     */
    public ToleranceSettingsPage selectAssumeTolerance() {
        pageUtils.waitForElementAndClick(assumeDefaultCheckbox);
        return this;
    }

    /**
     * Select specified default values
     *
     * @return current page object
     */
    public ToleranceValueSettingsPage selectSpecificDefaultValues() {
        pageUtils.waitForElementAndClick(specificCheckbox);
        return new ToleranceValueSettingsPage(driver);
    }

    /**
     * Select edit values button
     *
     * @return new page object
     */
    public ToleranceValueSettingsPage editValues() {
        pageUtils.waitForElementAndClick(editValuesButton);
        return new ToleranceValueSettingsPage(driver);
    }

    /**
     * Select use cad model checkbox
     *
     * @return current page object
     */
    public ToleranceSettingsPage selectUseCADModel() {
        pageUtils.waitForElementAndClick(cadModelCheckbox);
        return this;
    }

    /**
     * Set cad values
     *
     * @param minCADValue         - the minimum cad value
     * @param cadReplacementValue - the replacement cad value
     * @return current page object
     */
    public ToleranceSettingsPage replaceValues(String minCADValue, String cadReplacementValue) {
        replaceValuesCheckbox.click();
        minCADInput.clear();
        minCADInput.sendKeys(minCADValue);
        cadReplacementInput.clear();
        cadReplacementInput.sendKeys(cadReplacementValue);
        return this;
    }

    /**
     * Unchecks the replace less values option
     *
     * @return current page object
     */
    public ToleranceSettingsPage uncheckReplaceLessValuesButton() {
        if (replaceValuesCheckbox.getAttribute("value").contains("on")) {
            replaceValuesCheckbox.click();
        }
        return this;
    }

    /**
     * Check CAD values is selected
     *
     * @return current page object
     */
    public String isCADSelected(String attribute) {
        return pageUtils.waitForElementToAppear(cadModelCheckbox).getAttribute(attribute);
    }

    /**
     * Check CAD values is selected
     *
     * @return current page object
     */
    public String isAssumeSelected(String attribute) {
        return pageUtils.waitForElementToAppear(assumeDefaultCheckbox).getAttribute(attribute);
    }

}
