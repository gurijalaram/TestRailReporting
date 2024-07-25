package com.apriori.cid.ui.pageobjects.navtoolbars;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.web.app.util.PageUtils;

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
        typeAheadSelectAssignee(assigneeDropdown, "qa-assign-scenario-select-field", assignee);
        return this;
    }

    /**
     * Interacts with a dropdown and input the relevant info
     *
     * @param dropdownSelector - the selector
     * @param root             - the bottom level of the locator. this is the page the element is located on eg. can be in a modal dialog
     * @param locatorValue     - the locator value
     * @return current page object
     */
    private void typeAheadSelectAssignee(WebElement dropdownSelector, String root, String locatorValue) {
        if (!pageUtils.waitForElementToAppear(By.xpath(String.format("//div[@id='%s']//div[@class]", root))).getAttribute("textContent").equals(locatorValue)) {
            pageUtils.waitForElementAndClick(dropdownSelector);
            pageUtils.waitForElementToAppear(By.cssSelector(".assign-scenario-form .apriori-select div input")).sendKeys(locatorValue.split(" ")[0]);
            pageUtils.waitForElementAndClick(By.xpath(String.format("//div[@id='%s']//div[.='%s']//div[@id]", root, locatorValue)));
        }
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
