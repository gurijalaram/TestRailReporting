package main.java.pages.explore;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileUploadPage extends LoadableComponent<PrivateWorkspacePage> {

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

    public PrivateWorkspacePage uploadFile(String scenarioName, String filePath, String fileName) {
        inputScenarioName(scenarioName)
            .uploadFileDetails(filePath, fileName);
        selectOkButton();
        return new PrivateWorkspacePage(driver);
    }

    private FileUploadPage inputScenarioName(String scenarioName) {
        scenarioNameInput.click();
        pageUtils.clearInput(scenarioNameInput);
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    private FileUploadPage uploadFileDetails(String filePath, String fileName) {
        String filePathAndName = filePath + fileName;
        fileInput.sendKeys(filePathAndName);
        File file = new File(filePathAndName);
        String path = file.getAbsolutePath();
        fileInput.sendKeys(path);
        return this;
    }

    private PrivateWorkspacePage selectOkButton() {
        okButton.click();
        return new PrivateWorkspacePage(driver);
    }

    private PrivateWorkspacePage selectCancelButton() {
        cancelButton.click();
        return new PrivateWorkspacePage(driver);
    }
}
