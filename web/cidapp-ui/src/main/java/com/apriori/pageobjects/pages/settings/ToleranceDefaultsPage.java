package com.apriori.pageobjects.pages.settings;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ToleranceDefaultsPage extends LoadableComponent<ToleranceDefaultsPage> {

    @FindBy(xpath = "//button[.='Tolerance Defaults']")
    private WebElement toleranceTab;

    @FindBy(css = "[value='SYSTEMDEFAULT']")
    private WebElement systemRadioButton;

    @FindBy(css = "[value='PARTOVERRIDE']")
    private WebElement specificRadioButton;

    @FindBy(css = ".tolerance-defaults [data-icon='pencil']")
    private WebElement specificPencilButton;

    @FindBy(css = "[value='CAD']")
    private WebElement cadRadioButton;

    @FindBy(css = "[data-icon='square']")
    private WebElement replaceValuesCheckbox;

    @FindBy(css = "[name='tolerance.minCadToleranceThreshold']")
    private WebElement minCadInput;

    @FindBy(css = "[name='tolerance.cadToleranceReplacement']")
    private WebElement cadToleranceInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public ToleranceDefaultsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Tolerance tab was not selected", toleranceTab.getAttribute("class").contains("active"));
    }

    /**
     * Select system default
     *
     * @return current page object
     */
    public ToleranceDefaultsPage selectSystemDefault() {
        pageUtils.waitForElementAndClick(systemRadioButton);
        return this;
    }

    /**
     * Select specific value
     *
     * @return current page object
     */
    public ToleranceDefaultsPage selectSpecificValues() {
        pageUtils.waitForElementAndClick(specificRadioButton);
        return this;
    }

    public ToleranceOverridesPage editSpecificValues() {

        return new ToleranceOverridesPage(driver);
    }

    /**
     * Select cad
     *
     * @return current page object
     */
    public ToleranceDefaultsPage selectCad() {
        pageUtils.waitForElementAndClick(cadRadioButton);
        return this;
    }

    /**
     * Replace current values
     *
     * @param cadValue       - the cad value
     * @param toleranceValue - the cad tolerance
     * @return current page object
     */
    public ToleranceDefaultsPage replaceValues(String cadValue, String toleranceValue) {
        pageUtils.waitForElementAndClick(replaceValuesCheckbox);
        return this;
    }

    /**
     * Input cad value
     *
     * @param cadValue - the cad value
     * @return current page object
     */
    public ToleranceDefaultsPage inputCadValue(String cadValue) {
        pageUtils.clearInput(minCadInput);
        minCadInput.sendKeys(cadValue);
        return this;
    }

    /**
     * Input cad tolerance
     *
     * @param toleranceValue - the cad tolerance
     * @return current page object
     */
    public ToleranceDefaultsPage inputCadTolerance(String toleranceValue) {
        pageUtils.clearInput(cadToleranceInput);
        cadToleranceInput.sendKeys(toleranceValue);
        return this;
    }
}
