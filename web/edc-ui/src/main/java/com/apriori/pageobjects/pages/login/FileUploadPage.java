package com.apriori.pageobjects.pages.login;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileUploadPage {

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(css = ".input-group-prepend .action-button")
    private WebElement uploadPcbaButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public FileUploadPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
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
     * Click Upload PCBA button
     *
     * @return new page object
     */
    public UploadedFilePage clickUploadPCBA() {
        pageUtils.waitForElementAndClick(uploadPcbaButton);
        return new UploadedFilePage(driver);
    }
}
