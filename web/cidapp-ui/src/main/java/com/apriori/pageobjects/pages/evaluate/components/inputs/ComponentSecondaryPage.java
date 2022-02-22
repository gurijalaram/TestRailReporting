package com.apriori.pageobjects.pages.evaluate.components.inputs;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.inputs.CustomAttributesPage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryDFPage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryProcessesPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

@Slf4j
public class ComponentSecondaryPage extends LoadableComponent<ComponentSecondaryPage> {

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Secondary']")
    private WebElement secondaryTab;

    @FindBy(css = ".modal-body input[name='batchSize']")
    private WebElement batchSizeInput;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field-part-costing'] button")
    private WebElement secondaryProcessesPencil;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field-part-costing'] button")
    private WebElement secondaryProcesses;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field-part-costing'] .modal-select-content")
    private WebElement secondaryProcessesInput;

    @FindBy(css = "div[id='qa-secondary-digital-factory-select-part-costing'] button")
    private WebElement secondaryDFPencil;

    @FindBy(css = "div[id='qa-secondary-digital-factory-select-part-costing'] .modal-select-content")
    private WebElement secondaryDFInput;

    @FindBy(css = "[id='qa-secondary-digital-factory-select-part-costing']")
    private WebElement secDigitalFactoryList;

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Custom Attributes']")
    private WebElement customAttributesTab;

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Primary']")
    private WebElement primaryTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public ComponentSecondaryPage(WebDriver driver) {
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
        assertTrue("Secondary tab was not selected", secondaryTab.getAttribute("class").contains("active"));
        pageUtils.waitForElementToAppear(batchSizeInput);
    }

    /**
     * Enters the batch size
     *
     * @param batchSize - the batch size
     * @return current page object
     */
    public ComponentSecondaryPage enterBatchSize(String batchSize) {
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
    public CustomAttributesPage goToCustomAttributesTab() {
        pageUtils.waitForElementAndClick(customAttributesTab);
        return new CustomAttributesPage(driver);
    }

    /**
     * Opens primary tab
     *
     * @return new page object
     */
    public EvaluatePage goToPrimaryTab() {
        pageUtils.waitForElementAndClick(primaryTab);
        return new EvaluatePage(driver);
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
