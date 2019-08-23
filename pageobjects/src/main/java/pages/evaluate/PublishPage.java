package main.java.pages.evaluate;

import main.java.enums.WorkspaceEnum;
import main.java.pages.explore.ExplorePage;
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

public class PublishPage extends LoadableComponent<PublishPage> {

    private final Logger logger = LoggerFactory.getLogger(PublishPage.class);

    @FindBy(css = "[data-ap-scope='publishDialog'] h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "select[data-ap-field='status']")
    private WebElement statusDropdown;

    @FindBy(css = "select[data-ap-field='costMaturity']")
    private WebElement costMaturityDropdown;

    @FindBy(css = "select[data-ap-field='assignee']")
    private WebElement assigneeDropdown;

    @FindBy(css = "input[data-ap-field='locked']")
    private WebElement lockCheckBox;

    @FindBy(css = "button.gwt-SubmitButton")
    private WebElement publishButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PublishPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(statusDropdown);
    }

    /**
     * Selects status dropdown
     * @param status - dropdown status
     * @return current page object
     */
    public PublishPage selectStatus(String status) {
        new Select(statusDropdown).selectByVisibleText(status);
        return this;
    }

    /**
     * Selects the cost maturity dropdown
     * @param costMaturity - cost maturity dropdown
     * @return current page object
     */
    public PublishPage selectCostMaturity(String costMaturity) {
        new Select(costMaturityDropdown).selectByVisibleText(costMaturity);
        return this;
    }

    /**
     * Selects the assignee dropdown
     * @param assignee - assignee dropdown
     * @return current page object
     */
    public PublishPage selectAssignee(String assignee) {
        new Select(assigneeDropdown).selectByVisibleText(assignee);
        return this;
    }

    /**
     * Selects the publish button
     * @return new page object
     */
    public ExplorePage selectPublishButton() {
        publishButton.click();
        return new ExplorePage(driver).selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace());
    }

    /**
     * Selects the cancel button
     * @return new page object
     */
    public ExplorePage selectCancelButton() {
        cancelButton.click();
        return new ExplorePage(driver);
    }
}
