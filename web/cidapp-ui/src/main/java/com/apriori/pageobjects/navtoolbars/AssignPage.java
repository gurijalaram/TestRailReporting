package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssignPage extends LoadableComponent<AssignPage> {

    private static final Logger logger = LoggerFactory.getLogger(AssignPage.class);

    @FindBy(css = ".assign-scenario-form .apriori-select")
    private WebElement assigneeDropdown;

    @FindBy(css = ".assign-scenario-form .apriori-select input")
    private WebElement assigneeInput;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public AssignPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(assigneeDropdown);
    }

    /**
     * Uses type ahead to input the assignee
     *
     * @param assignee - the assignee
     * @return current page object
     */
    public AssignPage inputAssignee(String assignee) {
        pageUtils.typeAheadInput(assigneeDropdown, assigneeInput, assignee);
        return this;
    }

    /**
     * Checks the assignee is displayed
     *
     * @return true/false
     */
    public boolean isAssigneeDisplayed(String assignee) {
        By byAssignee = By.xpath(String.format("//form[@class='assign-scenario-form'] //div[.='%s']", assignee));
        pageUtils.invisibilityOfElements(driver.findElements(By.xpath("//form[@class='assign-scenario-form'] //div[.='Fetching users...']")));
        return pageUtils.waitForElementToAppear(byAssignee).isDisplayed();
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(klass);
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