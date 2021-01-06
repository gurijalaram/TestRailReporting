package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author cfrith
 */

public class FileUploadPage extends LoadableComponent<FileUploadPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(FileUploadPage.class);

    @FindBy(css = ".modal-content label")
    private WebElement componentLabel;

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(css = "input[name='scenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(css = "//div[class='Toastify__toast-body']")
    private WebElement alertWarning;

    @FindBy(xpath = "//input[@name='scenarioName']/following-sibling::span")
    private WebElement scenarioNameWarning;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public FileUploadPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(componentLabel);
    }

    /**
     * Input component details
     *
     * @param scenarioName - scenario name
     * @param filePath     - file path
     * @return current page object
     */
    public FileUploadPage inputComponentDetails(String scenarioName, File filePath) {
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
    private FileUploadPage inputScenarioName(String scenarioName) {
        scenarioNameInput.sendKeys(Keys.CONTROL + "a");
        scenarioNameInput.sendKeys(Keys.DELETE);
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Gets details of file for upload
     *
     * @param filePath - the file path
     * @return current page object
     */
    private FileUploadPage enterFilePath(File filePath) {
        fileInput.sendKeys(filePath.getAbsolutePath().replace("%20", " "));
        fileInput.sendKeys(filePath.getAbsolutePath().replace("%20", " "));
        return this;
    }

    /**
     * Gets alert warning
     * @return string
     */
    public String getAlertWarning() {
        return pageUtils.waitForElementToAppear(alertWarning).getText();
    }

    /**
     * Gets scenario name warning
     * @return string
     */
    public String getFieldWarningText() {
        scenarioNameInput.sendKeys(Keys.TAB);
        return pageUtils.waitForElementToAppear(scenarioNameWarning).getText();
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
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}