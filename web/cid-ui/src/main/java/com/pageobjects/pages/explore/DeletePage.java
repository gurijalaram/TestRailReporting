package com.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author cfrith
 */

public class DeletePage extends LoadableComponent<DeletePage> {

    private static final Logger logger = LoggerFactory.getLogger(DeletePage.class);

    @FindBy(css = "[data-ap-comp='deleteInputs'] .modal-content")
    private WebElement modalDialog;

    @FindBy(css = "input[data-ap-field='include']")
    private WebElement iterationsCheckbox;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "button.gwt-SubmitButton.btn.btn-primary")
    private WebElement deleteButton;

    @FindBy(css = ".popover-content .gwt-HTML")
    private List<WebElement> notificationPopover;

    private WebDriver driver;
    private PageUtils pageUtils;

    public DeletePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(modalDialog);
    }

    /**
     * Select delete iterations checkbox
     *
     * @return current page object
     */
    public DeletePage selectIterationsCheckbox() {
        pageUtils.waitForElementToAppear(iterationsCheckbox).click();
        return this;
    }

    public ExplorePage deleteScenarioIteration() {
        selectIterationsCheckbox();
        return deleteScenario();
    }

    /**
     * Selects the delete button
     *
     * @return new page object
     */
    public ExplorePage deleteScenario() {
        pageUtils.waitForElementToAppear(deleteButton).click();
        return new ExplorePage(driver);
    }

    /**
     * Deletes the comparison from the comparison page
     *
     * @return new page object
     */
    public ExplorePage deleteComparison() {
        pageUtils.waitForElementToAppear(deleteButton).click();
        return new ExplorePage(driver);
    }

    /**
     * Deletes the comparison from the explore page
     *
     * @return new page object
     */
    public ExplorePage deleteExploreComparison() {
        pageUtils.waitForElementToAppear(deleteButton).click();
        return new ExplorePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ExplorePage cancel() {
        pageUtils.waitForElementToAppear(cancelButton).click();
        return new ExplorePage(driver);
    }
}