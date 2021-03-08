package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SecondaryProcessesPage extends LoadableComponent<SecondaryProcessesPage> {

    private static final Logger logger = LoggerFactory.getLogger(SecondaryProcessesPage.class);

    @FindBy(xpath = "//div[normalize-space(@class)='tree selectable']")
    private WebElement processTree;

    @FindBy(xpath = "//input[@placeholder='Search...']")
    private WebElement searchInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public SecondaryProcessesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
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
        pageUtils.waitForElementAppear(processTree);
    }

    /**
     * Selects both the process type and process name of the secondary process
     *
     * @param processType - accepts a comma separated list of type string
     * @param processName - the process name
     * @return current page object
     */
    public SecondaryProcessesPage selectSecondaryProcess(String processType, String processName) {
        findProcessTypeAndName(processType, processName).click();
        return this;
    }

    /**
     * Finds the process type and name
     *
     * @param processType - the process type
     * @param processName - the process name
     * @return webelement
     */
    private WebElement findProcessTypeAndName(String processType, String processName) {
        return selectProcessType(processType).findProcessName(processName);
    }

    /**
     * Selects the secondary types dropdowns in the process tree
     *
     * @param processTypes - the secondary process type
     * @return current page object
     */
    private SecondaryProcessesPage selectProcessType(String processTypes) {
        Arrays.stream(Stream.of(processTypes)
            .map(processType -> processType.split(","))
            .collect(Collectors.toList())
            .get(0))
            .forEach(process -> {
                By secondaryProcess = By.xpath(String.format("//span[.='%s']/ancestor::span//button", process.trim()));
                pageUtils.scrollWithJavaScript(pageUtils.waitForElementToAppear(secondaryProcess), true).click();
            });
        return this;
    }

    /**
     * Select the secondary process checkbox
     *
     * @param processName - the secondary process
     * @return current page object
     */
    private WebElement findProcessName(String processName) {
        By processBox = By.xpath(String.format("//span[.='%s']/ancestor::span//label", processName.trim()));
        return pageUtils.scrollWithJavaScript(driver.findElement(processBox), true);
    }

    /**
     * Enter search input
     *
     * @param searchTerm - search term
     * @return current page object
     */
    public SecondaryProcessesPage search(String searchTerm) {
        pageUtils.waitForElementAppear(searchInput).clear();
        searchInput.sendKeys(searchTerm);
        return this;
    }

    /**
     * Select all
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectAll() {
        modalDialogController.selectAll();
        return this;
    }

    /**
     * Deselect all
     *
     * @return current page object
     */
    public SecondaryProcessesPage deselectAll() {
        modalDialogController.deselectAll();
        return this;
    }

    /**
     * Reset
     *
     * @return current page object
     */
    public SecondaryProcessesPage reset() {
        modalDialogController.reset();
        return this;
    }

    /**
     * Expand all
     *
     * @return current page object
     */
    public SecondaryProcessesPage expandAll() {
        modalDialogController.expandAll();
        return this;
    }

    /**
     * Collapse all
     *
     * @return current page object
     */
    public SecondaryProcessesPage collapseAll() {
        modalDialogController.collapseAll();
        return this;
    }

    /**
     * Get number of selected processes
     *
     * @return
     */
    public String getNoOfSelected() {
        By amounts = By.cssSelector("div[class='selected-amount'] span");
        String[] amount = pageUtils.waitForElementToAppear(amounts).getText().split("of");
        return amount[0].trim();
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
    public EvaluatePage cancel() {
        return modalDialogController.cancel(EvaluatePage.class);
    }
}
