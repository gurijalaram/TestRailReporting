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
public class AdvancedPage extends LoadableComponent<AdvancedPage> {

    @FindBy(css = "input[name='batchSize']")
    private WebElement batchSizeInput;

    @FindBy(css = ".secondary-process-modal-select-field button")
    private WebElement secondaryProcessesPencil;

    @FindBy(css = ".secondary-digital-factory-modal-select-field button")
    private WebElement secondaryDFPencil;

    @FindBy(css = ".secondary-digital-factory-modal-select-field")
    private WebElement secDigitalFactoryList;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Custom']")
    private WebElement customTab;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Basic']")
    private WebElement basicTab;

    @FindBy(css = ".secondary-process-modal-select-field .pill-box")
    private WebElement secondaryProcesses;

    @FindBy(css = "[id='qa-routing-modal-select-field'] [data-icon='pencil']")
    private WebElement routingSelectionPencil;

    @FindBy(css = "[id='qa-routing-modal-select-field']")
    private WebElement preferenceText;

    @FindBy(css = "[id = 'qa-routing-modal-select-field'] [class = 'modal-select-button w-100 btn btn-white']")
    private WebElement routingSelectionButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public AdvancedPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(batchSizeInput);
    }

    /**
     * Enters the batch size
     *
     * @param batchSize - the batch size
     * @return current page object
     */
    public AdvancedPage enterBatchSize(String batchSize) {
        inputsController.enterBatchSize(batchSizeInput, batchSize);
        return this;
    }

    /**
     * Opens the Routing selection modal
     *
     * @return new page object
     */
    public RoutingSelectionPage openRoutingSelection() {
        pageUtils.waitForElementAndClick(routingSelectionPencil);
        return new RoutingSelectionPage(driver);
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
    public SecondaryProcessesPage openSecondaryProcesses() {
        inputsController.openSecondaryProcesses(secondaryProcessesPencil);
        return new SecondaryProcessesPage(driver);
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
     * Opens basic tab
     *
     * @return new page object
     */
    public EvaluatePage goToBasicTab() {
        pageUtils.waitForElementAndClick(basicTab);
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

    /**
     * Checks if button is enabled
     *
     * @return boolean
     */
    public boolean isRoutingSelectionButtonEnabled() {
       return pageUtils.isElementClickable(routingSelectionButton);
    }

    /**
     * Get the Routing preference text
     *
     * @return - String
     */
    public String getRoutingSelectionSelected() {
        return pageUtils.waitForElementToAppear(preferenceText).getAttribute("textContent");
    }
}
