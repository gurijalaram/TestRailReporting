package com.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileOpenError extends LoadableComponent<FileOpenError> {

    private static final Logger logger = LoggerFactory.getLogger(FileOpenError.class);

    @FindBy(css = "div[data-ap-comp='genericDialog'] .modal-content")
    private WebElement modalDialog;

    @FindBy(css = "div[data-ap-scope='genericDialog'] .gwt-Label")
    private WebElement warningText;

    @FindBy(css = "//div[@data-ap-scope='genericDialog']//button[.='Cancel']")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public FileOpenError(WebDriver driver) {
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
    }

    /**
     * Gets the text in the error dialog
     *
     * @return webelement as string
     */
    public String getErrorText() {
        return pageUtils.waitForElementToAppear(warningText).getText();
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public FileUploadPage selectCancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new FileUploadPage(driver);
    }
}
