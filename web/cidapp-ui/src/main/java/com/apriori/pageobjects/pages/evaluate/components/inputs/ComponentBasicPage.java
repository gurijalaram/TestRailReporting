package com.apriori.pageobjects.pages.evaluate.components.inputs;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ComponentBasicPage extends LoadableComponent<ComponentBasicPage> {

    @FindBy(css = ".modal-body div[id='qa-process-group-select-field'] [data-icon='chevron-down']")
    private WebElement processGroupDropdown;

    @FindBy(css = ".modal-body [id='qa-process-group-select-field'] .apriori-select")
    private WebElement processGroupInput;

    @FindBy(css = ".modal-body div[id='qa-digital-factory-select-field'] [data-icon='chevron-down']")
    private WebElement digitalFactoryDropdown;

    @FindBy(css = ".modal-body [id='qa-digital-factory-select-field'] .apriori-select")
    private WebElement digitalFactoryInput;

    @FindBy(css = ".modal-body div[id='qa-material-modal-select-field'] .input-group-append button")
    private WebElement materialsPencil;

    @FindBy(css = ".modal-body [id='qa-material-modal-select-field'] .apriori-select")
    private WebElement materialInput;

    @FindBy(css = ".modal-body [id='qa-material-modal-select-field'] .placeholder")
    private WebElement materialInputPlaceholder;

    @FindBy(css = ".modal-body input[name='annualVolume']")
    private WebElement annualVolumeInput;

    @FindBy(css = ".modal-body input[name='productionLife']")
    private WebElement productionLifeInput;

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Basic']")
    private WebElement basicTab;

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Advanced']")
    private WebElement advancedTab;

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Custom']")
    private WebElement customTab;

    @FindBy(css = "button[data-testid='primary-button']")
    private WebElement applyAndCostBtn;

    @FindBy(css = "div[class='scenario-group-operations-success-message'] button")
    private WebElement closeBtn;

    @FindBy(css = "div[role='status']")
    private WebElement costingSpinner;

    @FindBy(css = "div[data-testid='loader']")
    private WebElement loadingSpinner;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;
    private String root = "modal-body";

    public ComponentBasicPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.inputsController = new InputsController(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    //Don't really need to do anything here
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Basic tab was not selected", basicTab.getAttribute("class").contains("active"));
        pageUtils.waitForElementToAppear(processGroupDropdown);
    }

    /**
     * Opens secondary input tab
     *
     * @return new page object
     */
    public ComponentAdvancedPage goToAdvancedInputsTab() {
        pageUtils.waitForElementAndClick(advancedTab);
        return new ComponentAdvancedPage(driver);
    }

    /**
     * Opens custom attributes tab
     *
     * @return new page object
     */
    public ComponentCustomPage goToCustomTab() {
        pageUtils.waitForElementAndClick(customTab);
        return new ComponentCustomPage(driver);
    }

    /**
     * Selects the pg dropdown
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public ComponentBasicPage selectProcessGroup(ProcessGroupEnum processGroup) {
        inputsController.selectInputsDropdown(processGroupDropdown, root, processGroup.getProcessGroup());
        return this;
    }

    /**
     * Get the text in the process group dropdown
     *
     * @return string
     */
    public String getProcessGroup() {
        return pageUtils.waitForElementToAppear(processGroupInput).getAttribute("textContent");
    }

    /**
     * Selects the vpe dropdown
     *
     * @param digitalFactory - the vpe
     * @return current page object
     */
    public ComponentBasicPage selectDigitalFactory(DigitalFactoryEnum digitalFactory) {
        inputsController.selectInputsDropdown(digitalFactoryDropdown, root, digitalFactory.getDigitalFactory());
        return this;
    }

    /**
     * Get the text in the digital factory dropdown
     *
     * @return string
     */
    public String getDigitalFactory() {
        return pageUtils.waitForElementToAppear(digitalFactoryInput).getAttribute("textContent");
    }

    /**
     * Opens the material selector table
     *
     * @return new page object
     */
    public MaterialSelectorPage openMaterialSelectorTable() {
        inputsController.openMaterialSelectorTable(materialsPencil);
        return new MaterialSelectorPage(driver);
    }

    /**
     * Get the text in the materials dropdown
     *
     * @return string
     */
    public String getMaterial() {
        return pageUtils.waitForElementToAppear(materialInput).getAttribute("textContent");
    }

    /**
     * Get the text in the materials dropdown
     *
     * @return string
     */
    public String getMaterialPlaceholder() {
        return pageUtils.waitForElementToAppear(materialInputPlaceholder).getAttribute("textContent");
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public ComponentBasicPage enterAnnualVolume(String annualVolume) {
        inputsController.enterAnnualVolume(annualVolumeInput, annualVolume);
        return this;
    }

    /**
     * Get the text in the annual volume input field
     *
     * @return int
     */
    public int getAnnualVolume() {
        return Integer.parseInt(pageUtils.waitForElementToAppear(annualVolumeInput).getAttribute("value"));
    }

    /**
     * Get the text in the annual volume input field
     *
     * @return String
     */
    public String getAnnualVolumePlaceholder() {
        return pageUtils.waitForElementToAppear(annualVolumeInput).getAttribute("placeholder");
    }

    /**
     * Enters the years of annual volume
     *
     * @param productionLife - the years
     * @return current page object
     */
    public ComponentBasicPage enterAnnualYears(String productionLife) {
        inputsController.enterAnnualYears(productionLifeInput, productionLife);
        return this;
    }

    /**
     * Get the text in the years input field
     *
     * @return string
     */
    public int getYears() {
        return Integer.parseInt(pageUtils.waitForElementToAppear(productionLifeInput).getAttribute("value"));
    }

    /**
     * Get the text in the years input field
     *
     * @return string
     */
    public String getYearsPlaceholder() {
        return pageUtils.waitForElementToAppear(productionLifeInput).getAttribute("placeholder");
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }

    /**
     * Apply & Cost
     *
     * @return generic page object
     */
    public <T> T applyAndCost(Class<T> klass) {

        return modalDialogController.applyCost(klass);
    }

    /**
     * Click Apply and Cost
     */
    public ComponentBasicPage clickApplyAndCost() {
        pageUtils.waitForElementAndClick(applyAndCostBtn);
        pageUtils.waitForElementToAppear(costingSpinner);
        pageUtils.waitForElementNotVisible(costingSpinner, 1);
        return this;
    }

    /**
     * Close
     *
     * @return generic page object
     */
    public <T> T close(Class<T> klass) {
        return modalDialogController.close(klass);
    }

    /**
     * Close
     *
     * @return generic page object
     */
    public ComponentsListPage clickCloseButton() {
        pageUtils.waitForElementAndClick(closeBtn);
        pageUtils.waitForElementNotVisible(loadingSpinner, 1);
        return new ComponentsListPage(driver);
    }
}
