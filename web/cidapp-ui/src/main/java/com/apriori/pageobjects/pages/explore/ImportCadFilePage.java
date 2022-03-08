package com.apriori.pageobjects.pages.explore;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import com.utils.MultiUpload;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

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
     * Input multiple component details
     *
     * @param multiUploadList - component details as a list
     * @return current page object
     */
    public ImportCadFilePage inputMultiComponentDetails(List<MultiUpload> multiUploadList) {
        multiUploadList.forEach(multiUpload -> {
            String file = multiUpload.getResourceFile().getName();

            enterMultiFilePath(multiUpload.getResourceFile())
                .inputMultiScenarioName(multiUpload.getScenarioName(), file);
        });
        return this;
    }

    /**
     * Upload multiple cad files
     *
     * @param multiUploadList - component details as a list
     * @return current page object
     */
    public ImportCadFilePage inputMultiComponents(List<MultiUpload> multiUploadList) {
        multiUploadList.forEach(multiUpload -> {
            enterMultiFilePath(multiUpload.getResourceFile());
        });
        return this;
    }

    /**
     * Input scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ImportCadFilePage inputScenarioName(String scenarioName) {
        pageUtils.waitForElementToAppear(scenarioNameInput);
        pageUtils.clearInput(scenarioNameInput);
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Input multiple scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    private ImportCadFilePage inputMultiScenarioName(String scenarioName, String file) {
        String[] component = file.split("\\.");
        By byMultiFileInput = By.cssSelector(String.format("input[name='scenarioNames.%s%s']", component[0], component[component.length - 1]));
        pageUtils.waitForElementToAppear(byMultiFileInput);
        pageUtils.setValueOfElement(pageUtils.waitForElementToAppear(byMultiFileInput), scenarioName);
        return this;
    }

    /**
     * Inputs details of the file to upload
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
     * Inputs details of the file to upload
     *
     * @param filePath - the file path
     * @return current page object
     */
    public ImportCadFilePage enterMultiFilePath(File filePath) {
        pageUtils.clearInput(fileInput);
        return enterFilePath(filePath);
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
     * Selects the option
     *
     * @param option - the option to select. Check box is either 'Apply to all' or 'Override existing scenario'
     * @return current page object
     */
    public ImportCadFilePage tick(String option) {
        By byCheckbox = byCheckbox(option);

        if (!driver.findElement(byCheckbox).findElement(By.cssSelector("svg")).getAttribute("data-icon").contains("check")) {
            pageUtils.waitForElementAndClick(byCheckbox);
        }
        return this;
    }

    /**
     * Selects one of the checkboxes based on the option provided
     *
     * @param option - the option to select. Check box is either 'Apply to all' or 'Override existing scenario'
     * @return current page object
     */
    public ImportCadFilePage unTick(String option) {
        By byCheckbox = byCheckbox(option);

        if (driver.findElement(byCheckbox).findElement(By.cssSelector("svg")).getAttribute("data-icon").contains("check")) {
            pageUtils.waitForElementAndClick(byCheckbox);
        }
        return this;
    }

    private By byCheckbox(String option) {
        return with(By.cssSelector(".checkbox-icon"))
            .near(By.xpath(String.format("//div[.='%s']", option)));
    }

    /**
     * Selects the submit button
     *
     * @return new page object
     */
    public CadFileStatusPage submit() {
        return modalDialogController.submit(submitButton, CadFileStatusPage.class);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }

    /**
     * Check if scenario name text box field is enabled
     *
     * @return - string
     */
    public List<String> scenarioNameTextBoxDisabled() {
        return driver.findElements(By.cssSelector(".cell-text [placeholder='Scenario Name']"))
            .stream()
            .map(x -> x.getAttribute("disabled"))
            .collect(Collectors.toList());
    }
}
