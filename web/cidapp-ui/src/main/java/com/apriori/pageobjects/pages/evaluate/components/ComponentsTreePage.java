package com.apriori.pageobjects.pages.evaluate.components;

import static org.junit.Assert.assertTrue;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ComponentsTreePage extends LoadableComponent<ComponentsTreePage> {

    @FindBy(css = "div[data-testid='loader']")
    private WebElement loadingSpinner;

    @FindBy(css = "[id='qa-scenario-list-card-view-button'] button")
    private WebElement treeViewButton;

    @FindBy(css = "[id='qa-scenario-list-table-view-button'] button")
    private WebElement tableViewButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ComponentsTreePage(WebDriver driver) {
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
        assertTrue("Tree View is not active", pageUtils.waitForElementToAppear(treeViewButton).getAttribute("class").contains("active"));
        pageUtils.waitForElementNotVisible(loadingSpinner, 1);
    }

    /**
     * Open table view
     *
     * @return new page object
     */
    public ComponentsTablePage selectTableView() {
        pageUtils.waitForElementAndClick(tableViewButton);
        return new ComponentsTablePage(driver);
    }
}
