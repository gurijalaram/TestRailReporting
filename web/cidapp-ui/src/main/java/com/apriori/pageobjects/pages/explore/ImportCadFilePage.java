package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author cfrith
 */

@Slf4j
public class ImportCadFilePage extends LoadableComponent<ImportCadFilePage> {

    @FindBy(css = ".modal-content label")
    private WebElement componentLabel;

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(css = "input[name='primaryScenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(css = "div[class='Toastify__toast-body']")
    private WebElement alertWarning;

    @FindBy(xpath = "//input[@name='scenarioName']/following-sibling::span")
    private WebElement scenarioNameWarning;

    @FindBy(css = ".form-action-buttons [type='submit']")
    private WebElement submitButton;

    @FindBy(css = "h4")
    private WebElement fileInputError;

    @FindBy(css = ".modal-body .checkbox-icon")
    private WebElement overrideCheckbox;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public ImportCadFilePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(componentLabel);
    }

    /**
     * Input component details
     *
     * @param scenarioName - scenario name
     * @param filePath     - file path
     * @return current page object
     */
    public ImportCadFilePage inputComponentDetails(String scenarioName, File filePath) {
        inputScenarioName(scenarioName)
            .enterFilePath(filePath);
        return this;
    }

    /**
     * Input scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    private ImportCadFilePage inputScenarioName(String scenarioName) {
        pageUtils.waitForElementToAppear(scenarioNameInput);
        pageUtils.clearInput(scenarioNameInput);
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Gets details of file for upload
     *
     * @param filePath - the file path
     * @return current page object
     */
    public ImportCadFilePage enterFilePath(File filePath) {
        try {
            fileInput.sendKeys(URLDecoder.decode(filePath.getAbsolutePath(), StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Gets alert warning
     *
     * @return string
     */
    public String getAlertWarning() {
        return pageUtils.waitForElementToAppear(alertWarning).getText();
    }

    /**
     * Gets scenario name warning
     *
     * @return string
     */
    public String getFieldWarningText() {
        return pageUtils.waitForElementToAppear(scenarioNameWarning).getText();
    }

    /**
     * Gets file input error
     *
     * @return string
     */
    public String getFileInputError() {
        return pageUtils.waitForElementToAppear(fileInputError).getText();
    }

    /**
     * Selects the override checkbox
     *
     * @return current page object
     */
    public ImportCadFilePage override() {
        pageUtils.waitForElementAndClick(overrideCheckbox);
        return this;
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(submitButton, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}