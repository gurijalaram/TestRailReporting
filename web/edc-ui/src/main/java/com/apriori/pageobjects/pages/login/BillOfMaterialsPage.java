package com.apriori.pageobjects.pages.login;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class BillOfMaterialsPage {

    @FindBy(css = ".part-card card")
    private WebElement fileMatch;

    private WebDriver driver;
    private PageUtils pageUtils;

    public BillOfMaterialsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    public BillOfMaterialsPage highlightItem() {
        pageUtils.waitForElementAndClick(fileMatch);
        return this;
    }
}
