package com.apriori.cid.ui.pageobjects.evaluate.components.inputs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cid.ui.pageobjects.common.InputsController;
import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.CustomPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.SecondaryDFPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.SecondaryProcessesPage;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

@Slf4j
public class ComponentAdvancedPage extends LoadableComponent<ComponentAdvancedPage> {

    @FindBy(xpath = "//div[@role='dialog']//button[.='Advanced']")
    private WebElement secondaryTab;

    @FindBy(xpath = "//div[@role='dialog']//button[.='Custom']")
    private WebElement customTab;

    @FindBy(xpath = "//div[@role='dialog']//button[.='Basic']")
    private WebElement basicTab;

    @FindBy(css = "[role='dialog'] input[name='batchSize']")
    private WebElement batchSizeInput;

    @FindBy(css = ".secondary-process-modal-select-field [data-icon='pencil']")
    private WebElement secondaryProcessesPencil;

    @FindBy(css = ".secondary-process-modal-select-field .input-group-append button")
    private WebElement secondaryProcesses;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field-part-costing'] .modal-select-content")
    private WebElement secondaryProcessesInput;

    @FindBy(css = ".secondary-digital-factory-modal-select-field [data-icon='pencil']")
    private WebElement secondaryDFPencil;

    @FindBy(css = ".secondary-digital-factory-modal-select-field .input-group-append button")
    private WebElement secondaryDFInput;

    @FindBy(css = "[id='qa-secondary-digital-factory-select-part-costing']")
    private WebElement secDigitalFactoryList;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public ComponentAdvancedPage(WebDriver driver) {
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
        assertTrue(secondaryTab.getAttribute("class").contains("active"), "Advanced tab was not selected");
        pageUtils.waitForElementToAppear(batchSizeInput);
    }

    /**
     * Enters the batch size
     *
     * @param batchSize - the batch size
     * @return current page object
     */
    public ComponentAdvancedPage enterBatchSize(String batchSize) {
        inputsController.enterBatchSize(batchSizeInput, batchSize);
        return this;
    }

    /**
     * Gets batch size
     *
     * @return int
     */
    public int getBatchSize() {
        return Integer.parseInt(pageUtils.waitForElementToAppear(batchSizeInput).getAttribute("value"));
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
     * Gets list of secondary processes
     *
     * @return list of string
     */
    public List<String> getSecondaryProcesses() {
        return inputsController.getSecondaryProcesses(secondaryProcesses);
    }

    /**
     * Gets the text in the secondary processes field
     *
     * @return list of string
     */
    public String getSecondaryProcessesInput() {
        return pageUtils.waitForElementToAppear(secondaryProcessesInput).getAttribute("textContent");
    }

    /**
     * Opens the digital factory dropdown
     *
     * @return new page object
     */
    public SecondaryDFPage openSecondaryDF() {
        pageUtils.waitForElementAndClick(secondaryDFPencil);
        return new SecondaryDFPage(driver);
    }

    /**
     * Gets list of secondary digital factory
     *
     * @return list as string
     */
    public List<String> getListOfSecondaryDigitalFactory() {
        return inputsController.getListOfDigitalFactory(secDigitalFactoryList, "Secondary Digital Factory");
    }

    /**
     * Gets the text in the digital factory field
     *
     * @return list of string
     */
    public String getSecondaryDigitalFactoryInput() {
        return pageUtils.waitForElementToAppear(secondaryDFInput).getAttribute("textContent");
    }

    /**
     * Opens custom attributes tab
     *
     * @return new page object
     */
    public CustomPage goToCustomTab() {
        pageUtils.waitForElementAndClick(customTab);
        return new CustomPage(driver);
    }

    /**
     * Opens primary tab
     *
     * @return new page object
     */
    public ComponentBasicPage goToBasicTab() {
        pageUtils.waitForElementAndClick(basicTab);
        return new ComponentBasicPage(driver);
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
