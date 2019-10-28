package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.utils.PageUtils;

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

    private final Logger logger = LoggerFactory.getLogger(FileUploadPage.class);

    @FindBy(css = "h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(css = "input[data-ap-field='scenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement okButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public FileUploadPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(modalDialog);
        pageUtils.waitForElementToAppear(cancelButton);
    }

    /**
     * Uploads a file
     * //* @param fileName - file name
     *
     * @param scenarioName - scenario name
     * @param filePath     - file path
     * @return current page object
     */
    public EvaluatePage uploadFile(String scenarioName, File filePath) {
        inputScenarioName(scenarioName)
            .uploadFileDetails(filePath);
        return selectOkButton();
    }

    /**
     * Input scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    private FileUploadPage inputScenarioName(String scenarioName) {
        pageUtils.waitForElementAndClick(scenarioNameInput);
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
    private FileUploadPage uploadFileDetails(File filePath) {
        fileInput.sendKeys(filePath.getAbsolutePath());
        fileInput.sendKeys(filePath.getAbsolutePath());
        return this;
    }

    /**
     * Selects the ok button
     *
     * @return new page object
     */
    private EvaluatePage selectOkButton() {
        pageUtils.waitForElementAndClick(okButton);
        return new EvaluatePage(driver);
    }

    /**
     * Select the cancel button
     *
     * @return current page object
     */
    private ExplorePage selectCancelButton() {
        cancelButton.click();
        return new ExplorePage(driver);
    }
}
