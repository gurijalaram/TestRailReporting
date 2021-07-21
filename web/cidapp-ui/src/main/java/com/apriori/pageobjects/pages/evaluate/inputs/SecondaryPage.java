package com.apriori.pageobjects.pages.evaluate.inputs;

import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

@Slf4j
public class SecondaryPage extends LoadableComponent<SecondaryPage> {

    @FindBy(css = "input[name='batchSize']")
    private WebElement batchSizeInput;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field'] button")
    private WebElement secondaryProcessesPencil;

    @FindBy(css = "div[id='qa-secondary-digital-factory-select'] button")
    private WebElement secondaryDFPencil;

    @FindBy(css = "[id='qa-secondary-digital-factory-select']")
    private WebElement secDigitalFactoryList;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Custom Attributes']")
    private WebElement customAttributesTab;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Primary']")
    private WebElement primaryTab;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field'] .pill-box")
    private WebElement secondaryProcesses;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public SecondaryPage(WebDriver driver) {
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
        pageUtils.waitForElementAppear(batchSizeInput);
    }

    /**
     * Enters the batch size
     *
     * @param batchSize - the batch size
     * @return current page object
     */
    public SecondaryPage enterBatchSize(String batchSize) {
        inputsController.enterBatchSize(batchSizeInput, batchSize);
        return this;
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
     * Opens the secondary processes page
     *
     * @return new page object
     */
    public <T> T openSecondaryProcesses(Class<T> klass) {
        inputsController.openMachiningProcesses(secondaryProcessesPencil);
        return PageFactory.initElements(driver, klass);
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
     * Gets batch size
     *
     * @return int
     */
    public int getBatchSize() {
        return Integer.parseInt(pageUtils.waitForElementToAppear(batchSizeInput).getAttribute("value"));
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
     * Gets list of secondary digital factory
     *
     * @return list as string
     */
    public List<String> getListOfSecondaryDigitalFactory() {
        return inputsController.getListOfDigitalFactory(secDigitalFactoryList, "Secondary Digital Factory");
    }
}
