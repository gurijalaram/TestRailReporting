package com.apriori.pageobjects.compare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.StatusIcon;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class SaveComparisonPage extends LoadableComponent<SaveComparisonPage> {

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

    @FindBy(css = "p[data-field='comparisonName']")
    private WebElement nameError;

    private PageUtils pageUtils;
    private WebDriver driver;
    private StatusIcon statusIcon;
    private ModalDialogController modalDialogController;

    public SaveComparisonPage(WebDriver driver) {
        super();
        this.driver = driver;
        this.statusIcon = new StatusIcon(driver);
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(saveComparisonLabel);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("Save Comparison", pageUtils.waitForElementToAppear(saveComparisonLabel).getAttribute("textContent"), "Save Comparison page was not displayed");
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
     * Check if Invalid Character error is displayed
     *
     * @return - Boolean of error visibility
     */
    public Boolean isInvalidCharacterErrorDisplayed() {
        return pageUtils.isElementDisplayed(nameError);
    }

    /**
     * Get text of Invalid Character Error
     *
     * @return - String of error
     */
    public String getInvalidCharacterErrorText() {
        return nameError.getText();
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
     * Get enabled state of Save/Submit button
     *
     * @return - Boolean showing enabled state of Save / Submit button
     */
    public Boolean isSaveEnabled() {
        return pageUtils.isElementEnabled(save);
    }

    /**
     * Click the Save button
     *
     * @return new Compare Page Object
     */
    public <T> T save(Class<T> klass) {
        pageUtils.waitForElementAndClick(save);
        pageUtils.waitForElementNotVisible(saveSpinner, 1);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Waits for the Saving Spinner to complete and disappear
     */
    public ComparePage waitForSavingSpinner() {
        pageUtils.waitForElementNotVisible(saveSpinner, 1);
        return new ComparePage(driver);
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
