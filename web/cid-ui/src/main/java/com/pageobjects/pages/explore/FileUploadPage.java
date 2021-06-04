package com.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author cfrith
 */

public class FileUploadPage extends LoadableComponent<FileUploadPage> {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadPage.class);

    @FindBy(css = "h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(css = "input[data-ap-field='scenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(xpath = "//div[@data-ap-comp='fileUpload']//button[.='OK']")
    private WebElement okButton;

    @FindBy(xpath = "//div[@data-ap-comp='fileUpload']//button[.='Cancel']")
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
     *
     * @param scenarioName - scenario name
     * @param filePath     - file path
     * @return current page object
     */
    public FileUploadPage inputFileDetails(String scenarioName, File filePath) {
        inputScenarioName(scenarioName)
            .enterFilePath(filePath);
        return this;
    }

    /**
     * Uploads a file
     *
     * @param scenarioName - scenario name
     * @param filePath     - file path
     * @return current page object
     */
    public FileUploadPage inputFileDetails(String scenarioName, List<File> filePath) {
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
        pageUtils.waitForElementAndClick(scenarioNameInput);
        scenarioNameInput.clear();
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
        try {
            fileInput.sendKeys(URLDecoder.decode(filePath.getAbsolutePath(), StandardCharsets.UTF_8.toString()));
            fileInput.sendKeys(URLDecoder.decode(filePath.getAbsolutePath(), StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Gets details of file for upload
     *
     * @param filePath - the file path
     * @return current page object
     */
    private FileUploadPage enterFilePath(List<File> filePath) {
        filePath.forEach(file -> {
            try {
                fileInput.sendKeys(URLDecoder.decode(file.getAbsolutePath(), StandardCharsets.UTF_8.toString()));
                fileInput.sendKeys(URLDecoder.decode(file.getAbsolutePath(), StandardCharsets.UTF_8.toString()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    /**
     * Selects the ok button
     *
     * @return generic page object
     */
    public <T> T selectOkButton(Class<T> className) {
        pageUtils.waitForElementAndClick(okButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T selectCancelButton(Class<T> className) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, className);
    }
}
