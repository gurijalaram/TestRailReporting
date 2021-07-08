package com.apriori.pageobjects.pages.settings;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ProductionDefaultsPage extends LoadableComponent<ProductionDefaultsPage> {

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
        assertTrue("Production Defaults is not displayed", productionDefaults.isDisplayed());
    }

    // TODO: 08/07/2021 cn - complete all javadocs
    /**
     * @param scenarioName
     * @return
     */
    public ProductionDefaultsPage inputScenarioName(String scenarioName) {
        pageUtils.clearInput(scenarioNameInput);
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * @param processGroup
     * @return
     */
    public ProductionDefaultsPage selectProcessGroup(String processGroup) {
        pageUtils.typeAheadSelect(processGroupDropdown, processGroup);
        return this;
    }

    /**
     * @param digitalFactory
     * @return
     */
    public ProductionDefaultsPage selectDigitalFactory(String digitalFactory) {
        pageUtils.typeAheadSelect(digitalFactoryDropdown, digitalFactory);
        return this;
    }

    /**
     * @param materialCatalog
     * @return
     */
    public ProductionDefaultsPage selectMaterialCatalog(String materialCatalog) {
        pageUtils.typeAheadSelect(materialCatalogDropdown, materialCatalog);
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
     * @param annualVolume
     * @return
     */
    public ProductionDefaultsPage inputAnnualVolume(String annualVolume) {
        pageUtils.clearInput(annualVolumeInput);
        annualVolumeInput.sendKeys(annualVolume);
        return this;
    }

    /**
     * @param years
     * @return
     */
    public ProductionDefaultsPage inputYears(String years) {
        pageUtils.clearInput(yearsInput);
        yearsInput.sendKeys(years);
        return this;
    }

    /**
     * @param batchSize
     * @return
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
    // TODO: cn - 08/07/2021 change all of these in this section because settings can be accessed from any page which means a generic PO has to be returned
    public EvaluatePage cancel() {
        return modalDialogController.cancel(EvaluatePage.class);
    }
}
