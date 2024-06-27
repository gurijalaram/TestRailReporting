package com.apriori.cid.ui.pageobjects.navtoolbars;

import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class BulkAnalysisToolbar extends MainNavBar {

    @FindBy(id = "qa-worksheet-create-new")
    private WebElement newButton;

    @FindBy(id = "qa-bcm-sub-header-info-button")
    private WebElement infoButton;

    @FindBy(id = "qa-bcm-sub-header-delete-button")
    private WebElement deleteButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public BulkAnalysisToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Click New button
     *
     * @return new page object
     */
    public DeletePage clickNew() {
        pageUtils.waitForElementAndClick(newButton);
        return new DeletePage(driver);
    }

    /**
     * Click Info button
     *
     * @return new page object
     */
    public InfoPage clickInfo() {
        pageUtils.waitForElementAndClick(infoButton);
        return new InfoPage(driver);
    }

    /**
     * Click Delete button
     *
     * @return new page object
     */
    public DeletePage clickDelete() {
        pageUtils.waitForElementAndClick(deleteButton);
        return new DeletePage(driver);
    }
}
