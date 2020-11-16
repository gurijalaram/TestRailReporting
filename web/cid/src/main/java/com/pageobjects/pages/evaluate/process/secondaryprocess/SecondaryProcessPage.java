package com.pageobjects.pages.evaluate.process.secondaryprocess;

import com.apriori.utils.PageUtils;
import com.apriori.utils.constants.Constants;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.process.ProcessSetupOptionsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */
public class SecondaryProcessPage extends LoadableComponent<SecondaryProcessPage> {

    private final Logger logger = LoggerFactory.getLogger(SecondaryProcessPage.class);

    @FindBy(css = ".secondary-treatments-panel-header-btn")
    private WebElement clearAllButton;

    @FindBy(css = "div[data-ap-comp='secondaryTreatmentsTable'] .gwt-Label")
    private WebElement treatmentTable;

    @FindBy(css = "div[data-ap-comp='secondaryTreatmentsTable'] div.v-grid-scroller-vertical")
    private WebElement processScroller;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = ".modal-footer button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public SecondaryProcessPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(treatmentTable);
    }

    /**
     * Selects both the process type and process name of the secondary process
     *
     * @param processType - accepts a comma separated list of type string
     * @param processName - the process name
     * @return current page object
     */
    public SecondaryProcessPage selectSecondaryProcess(String processType, String processName) {
        findProcessTypeAndName(processType, processName).click();
        return this;
    }

    /**
     * Finds and highlights the secondary process
     *
     * @param processType - the process type
     * @param processName - the process name
     * @return new page object
     */
    public ProcessSetupOptionsPage highlightSecondaryProcess(String processType, String processName) {
        findProcessTypeAndName(processType, processName);
        processNameLocator(processName).click();
        return new ProcessSetupOptionsPage(driver);
    }

    /**
     * Finds the secondary process, selects the checkbox and highlights the process
     *
     * @param processType - the process type
     * @param processName - the process name
     * @return new page object
     */
    public ProcessSetupOptionsPage selectHighlightSecondaryProcess(String processType, String processName) {
        selectSecondaryProcess(processType, processName);
        processNameLocator(processName).click();
        return new ProcessSetupOptionsPage(driver);
    }

    /**
     * Finds the secondary process
     *
     * @param processType - the process type
     * @param processName - the process name
     * @return current page object
     */
    public SecondaryProcessPage findSecondaryProcess(String processType, String processName) {
        selectProcessType(processType);
        processNameLocator(processName);
        return this;
    }

    /**
     * Selects the secondary types dropdowns in the process tree
     *
     * @param processType - the secondary process type
     * @return current page object
     */
    private SecondaryProcessPage selectProcessType(String processType) {
        String[] processTypes = processType.split(",");

        for (String process : processTypes) {
            By secondaryProcess = By.xpath("//div[@data-ap-comp='secondaryTreatmentsTable']//div[.='" + process.trim() + "']/ancestor::tr//span[@class='fa fa-caret-right']");
            pageUtils.scrollToElement(secondaryProcess, processScroller, Constants.ARROW_DOWN).click();
        }
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
     * Builds the locator for the process name
     *
     * @param processName - the process name
     * @return webelement
     */
    private WebElement processNameLocator(String processName) {
        return driver.findElement(By.xpath("//div[@data-ap-comp='secondaryTreatmentsTable']//div[.='" + processName.trim() + "']"));
    }

    /**
     * Select the secondary process checkbox
     *
     * @param processName - the secondary process
     * @return current page object
     */
    private WebElement findProcessName(String processName) {
        By processBox = By.xpath("//div[@data-ap-comp='secondaryTreatmentsTable']//div[.='" + processName + "']/ancestor::tr//input[@class='gwt-SimpleCheckBox']");
        return pageUtils.scrollToElement(processBox, processScroller, Constants.ARROW_DOWN);
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public EvaluatePage apply() {
        pageUtils.waitForElementAndClick(applyButton);
        return new EvaluatePage(driver);
    }

    /**
     * Selects the clear all button
     *
     * @return this
     */
    public SecondaryProcessPage selectClearAll() {
        pageUtils.waitForElementAndClick(clearAllButton);
        return new SecondaryProcessPage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public EvaluatePage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new EvaluatePage(driver);
    }

    /**
     * Gets the attribute of the secondary process checkbox.
     *
     * @return true/false
     */

    public String getCheckboxStatus(String process) {
        return driver.findElement(By.xpath("//div[@data-ap-comp='secondaryTreatmentsTable']//div[.='" + process.trim() + "']/ancestor::tr//input")).getAttribute("outerHTML");
    }
}