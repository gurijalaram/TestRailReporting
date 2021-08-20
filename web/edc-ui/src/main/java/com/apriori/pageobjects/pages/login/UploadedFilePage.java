package com.apriori.pageobjects.pages.login;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class UploadedFilePage {

    @FindBy(css = "[data-icon='exclamation-circle']")
    private WebElement fileOne;

    private WebDriver driver;
    private PageUtils pageUtils;

    public UploadedFilePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    public BillOfMaterialsPage clickItemOne() {
        pageUtils.waitForElementAndClick(fileOne);
        return new BillOfMaterialsPage(driver);
    }
}
