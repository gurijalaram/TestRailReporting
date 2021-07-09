package com.apriori.pageobjects.pages.settings;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
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
public class ProductionDefaultsPage extends LoadableComponent<ProductionDefaultsPage> {

    @FindBy(xpath = "//button[.='Production Defaults']")
    private WebElement productionsTab;

    @FindBy(css = ".production-defaults")
    private WebElement productionDefaults;

    @FindBy(css = "[name='production.defaultScenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(css = "[id='qa-production-defaults-process-group-select'] .apriori-select")
    private WebElement processGroupDropdown;

    @FindBy(css = "[id='qa-production-defaults-digital-factory-select'] .apriori-select")
    private WebElement digitalFactoryDropdown;

    @FindBy(css = "[id='qa-production-defaults-material-catalog-select'] .apriori-select")
    private WebElement materialCatalogDropdown;

    @FindBy(css = "[id='qa-material-modal-select-field'] [data-icon='pencil']")
    private WebElement materialPencil;

    @FindBy(css = "[name='production.defaultAnnualVolume']")
    private WebElement annualVolumeInput;

    @FindBy(css = "[name='production.defaultProductionLife']")
    private WebElement yearsInput;

    @FindBy(css = "[name='production.defaultBatchSize']")
    private WebElement batchSizeInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public ProductionDefaultsPage(WebDriver driver) {
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
        assertTrue("Production Defaults tab is not active", productionsTab.getAttribute("class").contains("active"));
        assertTrue("Production Defaults is not displayed", productionDefaults.isDisplayed());
    }

    /**
     * Input scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ProductionDefaultsPage inputScenarioName(String scenarioName) {
        pageUtils.clearInput(scenarioNameInput);
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Select process group
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public ProductionDefaultsPage selectProcessGroup(ProcessGroupEnum processGroup) {
        pageUtils.typeAheadSelect(processGroupDropdown, processGroup.getProcessGroup());
        return this;
    }

    /**
     * Select digital factory
     *
     * @param digitalFactory - the digital factory
     * @return current page object
     */
    public ProductionDefaultsPage selectDigitalFactory(DigitalFactoryEnum digitalFactory) {
        pageUtils.typeAheadSelect(digitalFactoryDropdown, digitalFactory.getDigitalFactory());
        return this;
    }

    /**
     * Select material catalog
     *
     * @param materialCatalog - the material catalog
     * @return current page object
     */
    public ProductionDefaultsPage selectMaterialCatalog(DigitalFactoryEnum materialCatalog) {
        pageUtils.typeAheadSelect(materialCatalogDropdown, materialCatalog.getDigitalFactory());
        return this;
    }

    /**
     * Opens the material selector table
     *
     * @return new page object
     */
    public MaterialSelectorPage openMaterialSelectorTable() {
        pageUtils.waitForElementAndClick(materialPencil);
        return new MaterialSelectorPage(driver);
    }

    /**
     * Input annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public ProductionDefaultsPage inputAnnualVolume(String annualVolume) {
        pageUtils.clearInput(annualVolumeInput);
        annualVolumeInput.sendKeys(annualVolume);
        return this;
    }

    /**
     * Input years
     *
     * @param years - the years
     * @return current page object
     */
    public ProductionDefaultsPage inputYears(String years) {
        pageUtils.clearInput(yearsInput);
        yearsInput.sendKeys(years);
        return this;
    }

    /**
     * Input batch size
     *
     * @param batchSize - the batch size
     * @return current page object
     */
    public ProductionDefaultsPage inputBatchSize(String batchSize) {
        pageUtils.clearInput(batchSizeInput);
        batchSizeInput.sendKeys(batchSize);
        return this;
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}
