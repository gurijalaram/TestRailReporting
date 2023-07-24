package com.apriori.pageobjects.navtoolbars;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ModalDialogController;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class AssignPage extends LoadableComponent<AssignPage> {

    @FindBy(css = ".assign-scenario-form .apriori-select")
    private WebElement assigneeDropdown;

    @FindBy(css = ".assign-scenario-form .apriori-select input")
    private WebElement assigneeInput;

    @FindBy(css = ".assign-scenario-form [type='submit']")
    private WebElement submitButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public AssignPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(assigneeDropdown);
    }

    /**
     * Uses type ahead to input the assignee
     *
     * @param assignee - the assignee
     * @return current page object
     */
    public AssignPage selectAssignee(String assignee) {
        pageUtils.typeAheadSelect(assigneeDropdown, "qa-assign-scenario-select-field", assignee);
        return this;
    }

    /**
     * Checks the assignee is displayed
     *
     * @return true/false
     */
    public boolean isAssigneeDisplayed(String assignee) {
        By byAssignee = By.xpath(String.format("//form[@class='assign-scenario-form'] //div[.='%s']", assignee));
        pageUtils.waitForElementsToNotAppear(By.xpath("//form[@class='assign-scenario-form'] //div[.='Fetching users...']"));
        return pageUtils.waitForElementToAppear(byAssignee).isDisplayed();
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(submitButton, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}
