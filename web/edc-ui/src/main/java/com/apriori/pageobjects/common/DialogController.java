package com.apriori.pageobjects.common;

import com.apriori.PageUtils;
import com.apriori.pageobjects.pages.login.MatchedPartPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class DialogController {

    @FindBy(css = ".modal-footer .btn-outline-primary")
    private WebElement saveButton;

    @FindBy(css = ".modal-content .btn-outline-secondary")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public DialogController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Click on the Save button
     *
     * @return new page object
     */
    public MatchedPartPage save() {
        pageUtils.waitForElementAndClick(saveButton);
        return new MatchedPartPage(driver);
    }

    /**
     * Click the cancel button
     *
     * @return new page object
     */
    public MatchedPartPage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new MatchedPartPage(driver);
    }
}
