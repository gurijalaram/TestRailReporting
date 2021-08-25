package com.apriori.pageobjects.pages.login;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class UploadedFilePage extends LoadableComponent<UploadedFilePage> {

    @FindBy(css = "[data-icon='exclamation-circle']")
    private WebElement fileOne;

    @FindBy(id = "filter-button")
    private WebElement filterButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public UploadedFilePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(filterButton);
    }

    /**
     * Click on the Matched part
     *
     * @return new page object
     */
    public BillOfMaterialsPage selectMatchedPart() {
        pageUtils.waitForElementAndClick(fileOne);
        return new BillOfMaterialsPage(driver);
    }
}
