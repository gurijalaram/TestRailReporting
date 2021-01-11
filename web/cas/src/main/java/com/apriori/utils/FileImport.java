package com.apriori.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileImport {

    private final Logger LOGGER = LoggerFactory.getLogger(FileImport.class);

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    private WebDriver driver;
    private PageUtils pageUtils;

    public FileImport(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
    }

    /**
     * Import File
     *
     * @param filePath - the file path
     * @return current page object
     */
    public FileImport importFile(File filePath) {
        fileInput.sendKeys(filePath.getAbsolutePath());
        By card = By.xpath(String.format("//div[@class='card-header']//span[.='%s']", filePath.getName()));
        pageUtils.waitForElementToAppear(card);
        return this;
    }

    /**
     * Select card
     * @param fileName - file name
     * @return current page object
     */
    public FileImport selectCard(String fileName) {
        By card = By.xpath(String.format("//div[@class='card-header']//span[.='%s']", fileName));
        pageUtils.waitForElementAndClick(card);
        return this;
    }
}
