package com.apriori.utils;

import com.apriori.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FileImport {

    private static final Logger logger = LoggerFactory.getLogger(FileImport.class);

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
        try {
            driver.findElement(By.cssSelector("input[type='file']")).sendKeys(URLDecoder.decode(filePath.getAbsolutePath(), StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Select card
     * @param fileName - file name
     * @return current page object
     */
    public FileImport selectCard(String fileName) {
        By card = By.xpath(String.format("//div[@class='card-header']//div[.='%s']", fileName));
        pageUtils.actionClick(card);
        return this;
    }

    /**
     * Validates if license card is displayed
     *
     * @param fileName - the file name
     * @return - true or false
     */
    public boolean isCardDisplayed(String fileName) {
        return pageUtils.isElementDisplayed(By.xpath(String.format("//div[@class='card-header']//div[.='%s']", fileName)));
    }
}
