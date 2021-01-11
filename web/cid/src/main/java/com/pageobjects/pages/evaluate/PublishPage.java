package com.pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.WorkspaceEnum;

import com.pageobjects.pages.explore.ExplorePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class PublishPage extends LoadableComponent<PublishPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(PublishPage.class);

    @FindBy(css = "a[data-ap-comp='exploreButton']")
    private WebElement exploreButton;

    @FindBy(css = "[data-ap-scope='publishDialog'] h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "select[data-ap-field='status']")
    private WebElement statusDropdown;

    @FindBy(css = "select[data-ap-field='costMaturity']")
    private WebElement costMaturityDropdown;

    @FindBy(css = "select[data-ap-field='assignee']")
    private WebElement assigneeDropdown;

    @FindBy(css = "select[data-ap-field='lockStatus']")
    private WebElement lockDropdown;

    @FindBy(css = "input[data-ap-field='locked']")
    private WebElement lockCheckBox;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "button.gwt-SubmitButton")
    private WebElement publishButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PublishPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(modalDialog);
        pageUtils.waitForElementToAppear(statusDropdown);
    }

    /**
     * Selects status dropdown
     *
     * @param status - dropdown status
     * @return current page object
     */
    public PublishPage selectStatus(String status) {
        pageUtils.checkDropdownOptions(statusDropdown, status);
        new Select(statusDropdown).selectByVisibleText(status);
        return this;
    }

    /**
     * Selects the cost maturity dropdown
     *
     * @param costMaturity - cost maturity dropdown
     * @return current page object
     */
    public PublishPage selectCostMaturity(String costMaturity) {
        pageUtils.checkDropdownOptions(costMaturityDropdown, costMaturity);
        new Select(costMaturityDropdown).selectByVisibleText(costMaturity);
        return this;
    }

    /**
     * Selects the assignee dropdown
     *
     * @param assignee - assignee dropdown
     * @return current page object
     */
    public PublishPage selectAssignee(String assignee) {
        pageUtils.checkDropdownOptions(assigneeDropdown, assignee);
        new Select(assigneeDropdown).selectByVisibleText(assignee);
        return this;
    }

    /**
     * Ticks the lock option
     *
     * @return current page object
     */
    public PublishPage selectLock() {
        pageUtils.waitForElementAndClick(lockCheckBox);
        return this;
    }

    /**
     * Selects the publish button
     *
     * @return new page object
     */
    public ExplorePage selectPublishButton() {
        pageUtils.waitForElementAndClick(publishButton);
        pageUtils.checkElementAttribute(exploreButton, "className", "active-tab");
        return new ExplorePage(driver).selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace());
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ExplorePage selectCancelButton() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new ExplorePage(driver);
    }
}
