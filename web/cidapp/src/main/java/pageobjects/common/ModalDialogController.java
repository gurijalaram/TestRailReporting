package pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModalDialogController extends LoadableComponent<ModalDialogController> {

    private final Logger LOGGER = LoggerFactory.getLogger(ModalDialogController.class);

    @FindBy(id = "secondary-process-select-all-btn")
    private WebElement selectAllButton;

    @FindBy(id = "secondary-process-clear-all-btn")
    private WebElement deselectAllbutton;

    @FindBy(id = "secondary-process-reset-btn")
    private WebElement resetButton;

    @FindBy(id = "secondary-process-reset-btn")
    private WebElement clearAllButton;

    @FindBy(css = "button[title='Expand all']")
    private WebElement expandAllButton;

    @FindBy(css = "button[title='Collapse all']")
    private WebElement collapseAllButton;

    @FindBy(xpath = "//div[@class='modal-content']//button[.='Submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//div[@class='modal-content']//button[.='Cancel']")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ModalDialogController(WebDriver driver) {
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
        pageUtils.waitForElementAppear(cancelButton);
        pageUtils.waitForElementAppear(submitButton);
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        pageUtils.javaScriptClick(submitButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> className) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Select all
     *
     * @return current page object
     */
    public ModalDialogController selectAll() {
        pageUtils.waitForElementAndClick(selectAllButton);
        return this;
    }

    /**
     * Deselect all
     *
     * @return current page object
     */
    public ModalDialogController deselectAll() {
        pageUtils.waitForElementAndClick(deselectAllbutton);
        return this;
    }

    /**
     * Reset
     *
     * @return current page object
     */
    public ModalDialogController reset() {
        pageUtils.waitForElementAndClick(resetButton);
        return this;
    }

    /**
     * Expand all
     *
     * @return current page object
     */
    public ModalDialogController expandAll() {
        pageUtils.waitForElementAndClick(expandAllButton);
        return this;
    }

    /**
     * Collapse all
     *
     * @return current page object
     */
    public ModalDialogController collapseAll() {
        pageUtils.waitForElementAndClick(collapseAllButton);
        return this;
    }
}