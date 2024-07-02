package com.apriori.cid.ui.pageobjects.bulkanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cid.ui.pageobjects.common.InputsController;
import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.cid.ui.pageobjects.evaluate.MaterialSelectorPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.AdvancedPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.CustomPage;
import com.apriori.cid.ui.pageobjects.explore.CadFileStatusPage;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class SetInputsModalPage extends LoadableComponent<SetInputsModalPage> {

    @FindBy(css = "[role='dialog'] h2")
    private WebElement headerText;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Advanced']")
    private WebElement advancedTab;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Custom']")
    private WebElement customTab;

    @FindBy(css = "div[id='qa-process-group-select-field'] [data-icon='chevron-down']")
    private WebElement processGroupDropdown;

    @FindBy(css = "div[id='qa-process-group-select-field'] input")
    private WebElement processGroupInput;

    @FindBy(css = "div[id='qa-digital-factory-select-field'] [data-icon='chevron-down']")
    private WebElement digitalFactoryDropdown;

    @FindBy(css = "div[id='qa-digital-factory-select-field'] input")
    private WebElement digitalFactoryInput;

    @FindBy(css = "div[id='qa-material-modal-select-field'] button")
    private WebElement materialsPencil;

    @FindBy(css = ".material-summary-card.card input")
    private WebElement materialName;

    @FindBy(css = ".MuiFormControlLabel-root [data-testid='checkbox']")
    private WebElement machinePartCheckbox;

    @FindBy(css = "input[name='annualVolume']")
    private WebElement annualVolumeInput;

    @FindBy(css = "input[name='productionLife']")
    private WebElement productionLifeInput;

    @FindBy(css = "[data-testid='close-button']")
    private WebElement closeButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;
    private InputsController inputsController;

    public SetInputsModalPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.inputsController = new InputsController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("Set Inputs", pageUtils.waitForElementToAppear(headerText).getAttribute("textContent"), "Set inputs modal was not displayed");
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
     * Ticks the Do not machine this part checkbox
     *
     * @return current page object
     */
    public SetInputsModalPage tickDoNotMachinePart() {
        inputsController.tickDoNotMachinePart(machinePartCheckbox);
        return this;
    }


    /**
     * Selects the pg dropdown
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public SetInputsModalPage selectProcessGroup(ProcessGroupEnum processGroup) {
        inputsController.selectInputsDropdown(processGroupDropdown, "qa-process-group-select-field", processGroup.getProcessGroup());
        return this;
    }

    /**
     * Selects the digital factory dropdown
     *
     * @param digitalFactory - the digital factory
     * @return current page object
     */
    public SetInputsModalPage selectDigitalFactory(DigitalFactoryEnum digitalFactory) {
        inputsController.selectInputsDropdown(digitalFactoryDropdown, "qa-digital-factory-select-field", digitalFactory.getDigitalFactory());
        return this;
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public SetInputsModalPage enterAnnualVolume(String annualVolume) {
        inputsController.enterAnnualVolume(annualVolumeInput, annualVolume);
        return this;
    }

    /**
     * Checks if annual volume input is enabled
     *
     * @return boolean
     */
    public boolean isAnnualVolumeInputEnabled() {
        return pageUtils.isElementEnabled(annualVolumeInput);
    }

    /**
     * Enters the years of annual volume
     *
     * @param productionLife - the years
     * @return current page object
     */
    public SetInputsModalPage enterAnnualYears(String productionLife) {
        inputsController.enterAnnualYears(productionLifeInput, productionLife);
        return this;
    }

    /**
     * Opens advanced input tab
     *
     * @return new page object
     */
    public AdvancedPage goToAdvancedTab() {
        pageUtils.javaScriptClick(pageUtils.waitForElementToAppear(advancedTab));
        return new AdvancedPage(driver);
    }

    /**
     * Opens custom tab
     *
     * @return new page object
     */
    public CustomPage goToCustomTab() {
        pageUtils.waitForElementAndClick(customTab);
        return new CustomPage(driver);
    }

    /**
     * Close the import modal dialog
     *
     * @return new page object
     */
    // FIXME: 02/07/2024 cn - this is a temporary workaround. this should really return cadfilestatuspage then click close which means all callers will need updated
    public WorksheetsExplorePage clickClose() {
        pageUtils.waitForElementAndClick(closeButton);
        return new WorksheetsExplorePage(driver);
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
     * Select the save button
     *
     * @return generic page object
     */
    // FIXME: 02/07/2024 cn - this is a temporary workaround. this should really return cadfilestatuspage then click close which means all callers will need updated
    public SetInputsModalPage save() {
        modalDialogController.save(CadFileStatusPage.class);
        return this;
    }
}
