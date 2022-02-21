package com.apriori.pageobjects.pages.evaluate.components.inputs;

import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
import com.apriori.pageobjects.pages.evaluate.inputs.CustomAttributesPage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ComponentPrimaryPage extends LoadableComponent<ComponentPrimaryPage> {

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

    @FindBy(css = ".modal-body input[name='annualVolume']")
    private WebElement annualVolumeInput;

    @FindBy(css = ".modal-body input[name='productionLife']")
    private WebElement productionLifeInput;

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Secondary']")
    private WebElement secondaryTab;

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Custom Attributes']")
    private WebElement customAttributesTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public ComponentPrimaryPage(WebDriver driver) {
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

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(processGroupDropdown);
    }

    /**
     * Opens secondary input tab
     *
     * @return new page object
     */
    public SecondaryPage openSecondaryInputsTab() {
        pageUtils.waitForElementAndClick(secondaryTab);
        return new SecondaryPage(driver);
    }

    /**
     * Opens custom attributes tab
     *
     * @return new page object
     */
    public CustomAttributesPage openCustomAttributesTab() {
        pageUtils.waitForElementAndClick(customAttributesTab);
        return new CustomAttributesPage(driver);
    }

    /**
     * Selects the pg dropdown
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public ComponentPrimaryPage selectProcessGroup(ProcessGroupEnum processGroup) {
        pageUtils.waitForElementAndClick(processGroupDropdown);
        pageUtils.javaScriptClick(pageUtils.waitForElementToAppear(By.xpath(String.format("//div[@id='modal-body']//div[.='%s']//div[@id]", processGroup.getProcessGroup()))));
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
    public ComponentPrimaryPage selectDigitalFactory(DigitalFactoryEnum digitalFactory) {
        pageUtils.waitForElementAndClick(digitalFactoryDropdown);
        pageUtils.javaScriptClick(pageUtils.waitForElementToAppear(By.xpath(String.format("//div[@id='modal-body']//div[.='%s']//div[@id]", digitalFactory.getDigitalFactory()))));
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
     * Enters the annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public ComponentPrimaryPage enterAnnualVolume(String annualVolume) {
        inputsController.enterAnnualVolume(annualVolumeInput, annualVolume);
        return this;
    }

    /**
     * Get the text in the annual volume input field
     *
     * @return string
     */
    public String getAnnualVolume() {
        return pageUtils.waitForElementToAppear(annualVolumeInput).getAttribute("textContent");
    }

    /**
     * Enters the years of annual volume
     *
     * @param productionLife - the years
     * @return current page object
     */
    public ComponentPrimaryPage enterAnnualYears(String productionLife) {
        inputsController.enterAnnualYears(productionLifeInput, productionLife);
        return this;
    }

    /**
     * Get the text in the years input field
     *
     * @return string
     */
    public String getYears() {
        return pageUtils.waitForElementToAppear(productionLifeInput).getAttribute("textContent");
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
     * Close
     *
     * @return generic page object
     */
    public <T> T close(Class<T> klass) {
        return modalDialogController.close(klass);
    }
}
