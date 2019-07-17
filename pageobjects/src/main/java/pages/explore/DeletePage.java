package main.java.pages.explore;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeletePage extends LoadableComponent<DeletePage> {

    private final Logger logger = LoggerFactory.getLogger(DeletePage.class);

    @FindBy(css = "[data-ap-comp='deleteInputs'] .modal-content")
    private WebElement modalDialog;

    @FindBy(css = "input[data-ap-field='include']")
    private WebElement iterationsCheckbox;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "button.gwt-SubmitButton.btn.btn-primary")
    private WebElement deleteButton;

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
     * @return current page object
     */
    public DeletePage selectIterationsCheckbox() {
        iterationsCheckbox.click();
        return this;
    }

    /**
     * Selects the delete button
     * @return new page object
     */
    public ExplorePage deleteScenario() {
        deleteButton.click();
        return new ExplorePage(driver);
    }

    /**
     * Selects the cancel button
     * @return new page object
     */
    public ExplorePage cancel() {
        cancelButton.click();
        return new ExplorePage(driver);
    }

}
