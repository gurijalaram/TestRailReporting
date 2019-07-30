package main.java.pages.explore;

import main.java.utils.PageUtils;
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

public class AssignPage extends LoadableComponent<AssignPage> {

    private final Logger logger = LoggerFactory.getLogger(AssignPage.class);

    @FindBy(css = "h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "select[data-ap-field='assignee']")
    private WebElement assigneeDropdown;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement saveButton;

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public AssignPage(WebDriver driver) {
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
     * Selects the assignee dropdown
     * @param assignee - the assignee
     * @return current page object
     */
    public AssignPage selectAssignee(String assignee) {
        pageUtils.waitForElementToBeClickable(assigneeDropdown).click();
        new Select(assigneeDropdown).selectByVisibleText(assignee);
        return this;
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public ExplorePage update() {
        saveButton.click();
        return new ExplorePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ExplorePage cancel() {
        cancelButton.click();
        return new ExplorePage(driver);
    }
}
