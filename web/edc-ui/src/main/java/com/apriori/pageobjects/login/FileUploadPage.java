package com.apriori.pageobjects.login;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileUploadPage extends EagerPageComponent<FileUploadPage> {

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(css = ".input-group-prepend .action-button")
    private WebElement uploadPcbaButton;

    @FindBy(css = ".bill-of-materials-upload")
    private WebElement uploadSection;

    public FileUploadPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(uploadSection);
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Input component details
     *
     * @param filePath -file path
     * @return current page object
     */
    public FileUploadPage inputComponentDetails(File filePath) {
        enterFilePath(filePath);
        return this;
    }

    /**
     * Clicks Upload PCBA button
     *
     * @return new page object
     */
    public UploadedFilePage clickUploadPCBA() {
        getPageUtils().waitForElementAndClick(uploadPcbaButton);
        return new UploadedFilePage(getDriver());
    }
}
