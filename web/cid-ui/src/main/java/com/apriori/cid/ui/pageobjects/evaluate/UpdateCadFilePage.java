package com.apriori.cid.ui.pageobjects.evaluate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.web.app.util.PageUtils;

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
public class UpdateCadFilePage extends LoadableComponent<UpdateCadFilePage> {

    @FindBy(css = "[role='dialog'] .dialog-title")
    private WebElement componentLabel;

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(css = "div[class='Toastify__toast-body']")
    private WebElement alertWarning;

    @FindBy(css = "[data-testid='apriori-alert']")
    private WebElement associationAlert;

    @FindBy(css = ".form-action-buttons [type='submit']")
    private WebElement submitButton;

    @FindBy(css = "[data-testid='alert-messaging']")
    private WebElement fileInputError;

    @FindBy(css = "[id='modal-body']")
    private WebElement modalBody;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public UpdateCadFilePage(WebDriver driver) {
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
        assertEquals("Update CAD File", pageUtils.waitForElementToAppear(componentLabel).getAttribute("textContent"), "Update CAD File page was not displayed");
    }


    /**
     * Inputs details of the file to upload
     *
     * @param filePath - the file path
     * @return current page object
     */
    public UpdateCadFilePage enterFilePath(File filePath) {
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
     * Gets assembly association alert
     *
     * @return string
     */
    public String getAssociationAlert() {
        return pageUtils.waitForElementToAppear(associationAlert).getText();
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
     * Get CAD file updating status
     *
     * @return string
     */
    public String getCadUpdateStatus() {
        return pageUtils.waitForElementToAppear(modalBody).getAttribute("textContent");
    }

    /**
     * Selects the submit button
     *
     * @return new page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(submitButton, klass);
    }

    /**
     * Select the Try Again button
     *
     * @return current page object
     */
    public UpdateCadFilePage tryAgain() {
        return modalDialogController.tryAgain(UpdateCadFilePage.class);
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

