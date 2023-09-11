package com.apriori.pageobjects.navtoolbars;

import com.apriori.PageUtils;
import com.apriori.pageobjects.compare.CompareExplorePage;
import com.apriori.pageobjects.compare.ComparePage;
import com.apriori.pageobjects.compare.ModifyComparisonPage;
import com.apriori.pageobjects.compare.SaveComparisonPage;
import com.apriori.pageobjects.explore.ImportCadFilePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

@Slf4j
public class CompareToolbar extends MainNavBar {

    @FindBy(id = "qa-sub-header-new-comparison")
    private WebElement newButton;

    @FindBy(id = "qa-sub-header-modify-button")
    private WebElement modifyButton;

    @FindBy(xpath = "//div[@id='qa-sub-header-delete-button']//span[.='Delete']/../..")
    private WebElement deleteButton;

    @FindBy(xpath = "//div[@id='qa-sub-header-delete-button']//span[.='Rename']/../..")
    private WebElement renameButton;

//    @FindBy(id = "qa-sub-header-delete-button")
//    private WebElement deleteButton;
//
//    @FindBy(id = "qa-sub-header-rename-button")
//    private WebElement renameButton;

    @FindBy(id = "qa-sub-header-refresh-view-button")
    private WebElement refreshButton;

    @FindBy(css = "div[id='qa-sub-header-save-as-button'] button")
    private WebElement saveButton;

    @FindBy(css = "div[data-testid='apriori-alert'] div[class*='MuiAlert-icon']")
    private WebElement saveStatusIcon;

    @FindBy(css = "div[data-testid='apriori-alert'] div[class*='MuiAlert-message']")
    private WebElement saveStatusText;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CompareToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(newButton);
    }

    /**
     * Create new Empty Comparison
     *
     * @return Comparison View Page Object
     */
    public ComparePage newComparison() {
        pageUtils.waitForElementAndClick(newButton);
        return new ComparePage(driver);
    }

    /**
     * Modify a scenario
     *
     * @return new page object
     */
    public ModifyComparisonPage modify() {
        pageUtils.waitForElementAndClick(modifyButton);
        return new ModifyComparisonPage(driver);
    }

    /**
     * Check enabled status of Delete button
     *
     * @return - Boolean representation of Delete button state
     */
    public Boolean isDeleteEnabled() {
        return pageUtils.isElementEnabled(deleteButton);
    }

    /**
     * Click Delete button
     *
     * @return - Confirm deletion modal PO
     */
    public DeletePage delete() {
        pageUtils.waitForElementAndClick(deleteButton);
        return new DeletePage(driver);
    }

    /**
     * Check enabled status of Rename button
     *
     * @return - Boolean representation of Delete button state
     */
    public Boolean isRenameEnabled() {
        return pageUtils.isElementEnabled(deleteButton);
    }

    /**
     * Click Rename button
     *
     * @return - Save Comparison modal PO
     */
    public SaveComparisonPage rename() {
        pageUtils.waitForElementAndClick(deleteButton);
        return new SaveComparisonPage(driver);
    }

    /**
     * Save new Comparison
     * To be used when the comparison is new and will require input of a unique name
     *
     * @return Save Comparison Page Object
     */
    public SaveComparisonPage saveNew() {
        pageUtils.waitForElementAndClick(saveButton);
        return new SaveComparisonPage(driver);
    }

    /**
     * Save changes to Comparison
     * To be used when the comparison already exists
     *
     * @return Compare Page Object
     */
    public SaveComparisonPage saveChanges() {
        pageUtils.waitForElementAndClick(saveButton);
        return new SaveComparisonPage(driver);
    }

    /**
     * Check enabled state of Save button
     *
     * @return Boolean
     */
    public Boolean saveButtonEnabled() {
        return pageUtils.isElementEnabled(saveButton);
    }

    /**
     * Click the refresh button
     *
     * @return New copy of current page object
     */
    public <T> T clickRefresh(Class<T> klass) {
        pageUtils.waitForElementAndClick(refreshButton);
        return PageFactory.initElements(driver, klass);
    }

}
