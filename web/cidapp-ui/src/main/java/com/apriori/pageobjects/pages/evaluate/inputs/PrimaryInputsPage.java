package com.apriori.pageobjects.pages.evaluate.inputs;

import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
import com.apriori.pageobjects.pages.evaluate.SecondaryProcessesPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PrimaryInputsPage extends LoadableComponent<PrimaryInputsPage> {

    private final Logger logger = LoggerFactory.getLogger(PrimaryInputsPage.class);

    @FindBy(css = ".inputs-container input[name='annualVolume']")
    private WebElement annualVolumeInput;

    @FindBy(css = ".inputs-container input[name='productionLife']")
    private WebElement productionLifeInput;

    @FindBy(id = ".inputs-container [id='qa-scenario-select-field']")
    private WebElement currentScenarioDropdown;

    @FindBy(css = ".inputs-container div[id='qa-process-group-select-field'] [data-icon='chevron-down']")
    private WebElement processGroupDropdown;

    @FindBy(css = ".inputs-container div[id='qa-process-group-select-field'] input")
    private WebElement processGroupInput;

    @FindBy(css = ".inputs-container div[id='qa-digital-factory-select-field'] [data-icon='chevron-down']")
    private WebElement digitalFactoryDropdown;

    @FindBy(css = ".inputs-container div[id='qa-secondary-process-modal-select-field'] .pill-box")
    private WebElement secondaryProcessBox;

    @FindBy(css = ".inputs-container div[id='qa-secondary-process-modal-select-field'] .badge-pill")
    private List<WebElement> secondaryProcesses;

    @FindBy(css = ".inputs-container div[id='qa-secondary-process-modal-select-field'] .input-group-append")
    private WebElement secondaryProcessesPencil;

    @FindBy(css = ".inputs-container div[id='qa-material-modal-select-field'] .input-group-append")
    private WebElement materialsPencil;

    @FindBy(xpath = "//div[@class='inputs-container']//button[.='Secondary']")
    private WebElement secondaryTab;

    @FindBy(xpath = "//div[@class='inputs-container']//button[.='Custom Attributes']")
    private WebElement customAttributesTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public PrimaryInputsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.inputsController = new InputsController(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(processGroupDropdown);
    }

    /**
     * Opens secondary input tab
     *
     * @return new page object
     */
    public SecondaryInputsPage openSecondaryInputsTab() {
        pageUtils.waitForElementAndClick(secondaryTab);
        return new SecondaryInputsPage(driver);
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
    public PrimaryInputsPage selectProcessGroup(ProcessGroupEnum processGroup) {
        inputsController.selectProcessGroup(processGroupDropdown, processGroup);
        return this;
    }

    /**
     * Selects the vpe dropdown
     *
     * @param digitalFactory - the vpe
     * @return current page object
     */
    public PrimaryInputsPage selectDigitalFactory(DigitalFactoryEnum digitalFactory) {
        inputsController.selectDigitalFactory(digitalFactoryDropdown, digitalFactory);
        return this;
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
     * Opens the secondary processes page
     *
     * @return new page object
     */
    public SecondaryProcessesPage openSecondaryProcesses() {
        inputsController.openSecondaryProcesses(secondaryProcessesPencil);
        return new SecondaryProcessesPage(driver);
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public PrimaryInputsPage enterAnnualVolume(String annualVolume) {
        inputsController.enterAnnualVolume(annualVolumeInput, annualVolume);
        return this;
    }

    /**
     * Enters the years of annual volume
     *
     * @param productionLife - the years
     * @return current page object
     */
    public PrimaryInputsPage enterAnnualYears(String productionLife) {
        inputsController.enterAnnualYears(productionLifeInput, productionLife);
        return this;
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
     * Cost
     *
     * @return current page object
     */
    public <T> T cost(Class<T> klass) {
        return modalDialogController.cost(klass);
    }
}
