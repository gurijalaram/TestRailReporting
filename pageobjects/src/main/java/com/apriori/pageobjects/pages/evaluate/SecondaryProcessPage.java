package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.utils.PageUtils;
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

    @FindBy(css = "input[data-ap-comp='plateThickness.radioButtons.standard")
    private WebElement standardRadioButton;

    @FindBy(css = "input[data-ap-comp='plateThickness.radioButtons.user']")
    private WebElement desiredRadioButton;

    @FindBy(css = "input[data-ap-field='plateThickness.modeValues.user.value']")
    private WebElement thicknessInput;

    @FindBy(css = "select[data-ap-field='platingMethod.modeValues.platingMethod.storedListValue']")
    private WebElement platingMethodSelect;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.btn.btn-default")
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
     * @param processType - accepts a comma separated list of type string
     * @param processName - the process name
     * @return current page object
     */
    public SecondaryProcessPage selectSecondaryProcess(String processType, String processName) {
        selectProcessType(processType);
        selectProcessName(processName).click();
        return this;
    }

    /**
     * Selects the secondary types dropdowns in the process tree
     * @param processType - the secondary process type
     * @return current page object
     */
    private SecondaryProcessPage selectProcessType(String processType) {
        String[] processTypes = processType.split(",");

        for (String process : processTypes) {
            By secondaryProcess = By.xpath("//div[@data-ap-comp='secondaryTreatmentsTable']//div[.='" + process.trim() + "']/ancestor::tr//span[@class='fa fa-caret-right']");
            pageUtils.scrollToElement(secondaryProcess, processScroller).click();
        }
        return this;
    }

    /**
     * Select the secondary process checkbox
     *
     * @param processName - the secondary process
     * @return current page object
     */
    private WebElement selectProcessName(String processName) {
        By processBox = By.xpath("//div[@data-ap-comp='secondaryTreatmentsTable']//div[.='" + processName + "']/ancestor::tr//input[@class='gwt-SimpleCheckBox']");
        return pageUtils.scrollToElement(processBox, processScroller);
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public EvaluatePage apply() {
        applyButton.click();
        return new EvaluatePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public EvaluatePage cancel() {
        cancelButton.click();
        return new EvaluatePage(driver);
    }
}