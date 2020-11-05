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
        return PageFactory.initElements(driver,className);
    }
}
