package main.java.pages.evaluate.designguidance.investigation;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadingPage extends LoadableComponent<ThreadingPage> {

    private final Logger logger = LoggerFactory.getLogger(ThreadingPage.class);

    @FindBy(css = ".modal-content")
    private WebElement threadDialog;

    @FindBy(css = "label[data-ap-field='cadThreaded']")
    private WebElement threadCADText;

    @FindBy(css = "select[data-ap-field='threadGcd']")
    private WebElement threadDropdown;

    @FindBy(css = "input[data-ap-field='threadLength']")
    private WebElement lengthInput;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ThreadingPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(threadDialog);
    }

    /**
     * Selects the thread dropdown option
     * @param option - the option
     * @return current page object
     */
    public ThreadingPage selectThreadDropdown(String option) {
        new Select(threadDropdown).selectByVisibleText(option);
        return this;
    }

    /**
     * Enters the thread length
     * @param length - the thread length
     * @return current page object
     */
    public ThreadingPage enterThreadLength(String length) {
        pageUtils.clearInput(lengthInput);
        lengthInput.sendKeys(length);
        return this;
    }

    /**
     * Gets the thread length
     * @return - the thread length
     */
    public String getThreadLength() {
        return pageUtils.waitForElementToAppear(lengthInput).getText();
    }

    /**
     * Selects the apply button
     * @return new page object
     */
    protected InvestigationPage apply() {
        applyButton.click();
        return new InvestigationPage(driver);
    }

    /**
     * Selects the cancel button
     * @return new page object
     */
    protected InvestigationPage cancel() {
        cancelButton.click();
        return new InvestigationPage(driver);
    }
}
