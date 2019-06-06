package main.java.pages.evaluate;

import main.java.pages.explore.PrivateWorkspacePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishPage extends LoadableComponent<PublishPage> {

    private final Logger logger = LoggerFactory.getLogger(PublishPage.class);

    @FindBy(css = "h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "select[data-ap-field='status']")
    private WebElement statusDropdown;

    @FindBy(css = "select[data-ap-field='costMaturity']")
    private WebElement costMaturityDropdown;

    @FindBy(css = "select[data-ap-field='locked']")
    private WebElement assigneeDropdown;

    @FindBy(css = "input[data-ap-field='assignee']")
    private WebElement lockCheckBox;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
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

    protected PublishPage selectStatus(String status) {
        new Select(statusDropdown).selectByVisibleText(status);
        return this;
    }

    protected PublishPage selectCostMaturity(String costMaturity) {
        new Select(costMaturityDropdown).selectByVisibleText(costMaturity);
        return this;
    }

    protected PublishPage selectAssignee(String assignee) {
        new Select(assigneeDropdown).selectByVisibleText(assignee);
        return this;
    }

    protected PrivateWorkspacePage selectPublishButton() {
        publishButton.click();
        return new PrivateWorkspacePage(driver);
    }

    protected PrivateWorkspacePage selectCancelButton() {
        cancelButton.click();
        return new PrivateWorkspacePage(driver);
    }
}
