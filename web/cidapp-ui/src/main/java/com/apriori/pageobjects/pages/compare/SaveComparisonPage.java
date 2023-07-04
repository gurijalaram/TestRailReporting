package com.apriori.pageobjects.pages.compare;

import static org.junit.Assert.assertEquals;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.StatusIcon;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveComparisonPage extends LoadableComponent<SaveComparisonPage> {

    private static final Logger logger = LoggerFactory.getLogger(CreateComparePage.class);

    @FindBy(css = ".MuiPaper-root h2")
    private WebElement saveComparisonLabel;

    @FindBy(css = "input[name='comparisonName']")
    private WebElement comparisonNameInput;

    @FindBy(css = "button[data-testid='secondary-button']")
    private WebElement cancel;

    @FindBy(css = "button[data-testid='primary-button']")
    private WebElement save;

    @FindBy(css = "div[role='status']")
    private WebElement saveSpinner;

    private PageUtils pageUtils;
    private WebDriver driver;
    private StatusIcon statusIcon;
    private ModalDialogController modalDialogController;

    public SaveComparisonPage(WebDriver driver) {
        super();
        this.driver = driver;
        this.statusIcon = new StatusIcon(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(saveComparisonLabel);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("Save Comparison page was not displayed", "Save Comparison", pageUtils.waitForElementToAppear(saveComparisonLabel).getAttribute("textContent"));
    }

    /**
     * Input given name to Comparison Name textbox
     *
     * @param comparisonName - The comparison name to be used
     * @return
     */
    public SaveComparisonPage inputName(String comparisonName) {
        pageUtils.clearValueOfElement(comparisonNameInput);
        comparisonNameInput.sendKeys(comparisonName);
        return this;
    }

    /**
     * Click the Cancel button
     *
     * @return new Compare Page Object
     */
    public ComparePage cancel() {
        pageUtils.waitForElementAndClick(cancel);
        return new ComparePage(driver);
    }

    /**
     * Click the Save button
     *
     * @return new Compare Page Object
     */
    public ComparePage save() {
        pageUtils.waitForElementAndClick(save);
        pageUtils.waitForElementNotVisible(saveSpinner, 1);
        return new ComparePage(driver);
    }

    /**
     * Click the Save button expecting an error
     *
     * @return new Compare Page Object
     */
    public SaveComparisonPage saveExpectingError() {
        pageUtils.waitForElementAndClick(save);
        pageUtils.waitForElementNotVisible(saveSpinner, 1);
        return this;
    }

    /**
     * Waits for the Saving Spinner to complete and disappear
     *
     */
    public void waitForSavingSpinner() {
        pageUtils.waitForElementNotVisible(saveSpinner, 1);
    }

    /**
     * Get Toastify Error.
     *
     * @return Toastify error object
     */
    public String getToastifyError() {
        return pageUtils.waitForElementToAppear(By.className("Toastify__toast-body")).getText();
    }
}