package com.apriori.pageobjects.pages.evaluate.materialutilization;

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

public class MaterialUtilizationPage extends LoadableComponent<MaterialUtilizationPage> {

    private final Logger logger = LoggerFactory.getLogger(MaterialUtilizationPage.class);

    @FindBy(css = "input[data-ap-field='materialUnitCostFlag']")
    private WebElement basicInfoCheckbox;

    @FindBy(css = "input[data-ap-field='materialUnitCostOverride']")
    private WebElement basicInfoInput;

    @FindBy(css = "input[data-ap-field='utilizationFlag']")
    private WebElement utilizationCheckBox;

    @FindBy(css = "input[data-ap-field='utilizationOverride']")
    private WebElement utilizationInput;

    @FindBy(css = "div[id='materialTable']")
    private WebElement materialTableInfo;

    @FindBy(xpath = "//span[contains(text(), 'Utilization')]/..")
    private WebElement utilizationsTabToggle;

    @FindBy(xpath = "//span[contains(text(), 'Material Properties')]/..")
    private WebElement materialPropertiesTabToggle;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public MaterialUtilizationPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(basicInfoCheckbox);
        pageUtils.waitForElementToAppear(utilizationCheckBox);
    }

    /**
     * Selects info check box
     *
     * @return current page object
     */
    public MaterialUtilizationPage selectInfoCheckBox() {
        basicInfoCheckbox.click();
        return this;
    }

    /**
     * Enter basic override value
     *
     * @param value - the value as string
     * @return current page object
     */
    public MaterialUtilizationPage enterBasicOverride(String value) {
        pageUtils.waitForElementToBeClickable(basicInfoInput);
        pageUtils.clearInput(basicInfoInput);
        basicInfoInput.sendKeys(value);
        return this;
    }

    /**
     * Select utilization check box
     *
     * @return current page object
     */
    public MaterialUtilizationPage selectUtilizationCheckBox() {
        utilizationCheckBox.click();
        return this;
    }

    /**
     * Enter utilization override value
     *
     * @param value - the value as string
     * @return current page object
     */
    public MaterialUtilizationPage enterUtilizationOverride(String value) {
        pageUtils.waitForElementToBeClickable(utilizationInput);
        pageUtils.clearInput(utilizationInput);
        utilizationInput.sendKeys(value);
        return this;
    }

    /**
     * Gets the info in the material table
     *
     * @return material table info as string
     */
    public String getMaterialTableInfo() {
        return pageUtils.waitForElementToAppear(materialTableInfo).getText();
    }

    /**
     * Gets string value which states if utilization panel is expanded or not
     *
     * @return string of utilization panel class name
     */
    public String utilizationPanelExpanded() {
        return utilizationsTabToggle.getAttribute("className");
    }

    /**
     * Toggles Utilization panel (open or closed)
     *
     * @return current page object
     */
    public MaterialUtilizationPage toggleUtilizationPanel() {
        utilizationsTabToggle.click();
        return this;
    }

    /**
     * Gets string value which states if material panel is expanded or not
     *
     * @return string of material properties panel class name
     */
    public String materialPanelExpanded() {
        return materialPropertiesTabToggle.getAttribute("className");
    }

    /**
     * Toggles Material Properties panel (open or closed)
     *
     * @return current page object
     */
    public MaterialUtilizationPage toggleMaterialPropertiesPanel() {
        materialPropertiesTabToggle.click();
        return this;
    }
}