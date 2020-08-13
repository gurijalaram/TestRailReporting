package customeradmin;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerAdminPage extends LoadableComponent<CustomerAdminPage> {

    private final Logger logger = LoggerFactory.getLogger(CustomerAdminPage.class);

    @FindBy(id = "qa-new-customer-link")
    private WebElement newCustomerButton;

    @FindBy(id = "qa-customer-counter")
    private WebElement customerCount;

    @FindBy(css = "svg[data-icon='list']")
    private WebElement listViewButton;

    @FindBy(css = "svg[data-icon='th']")
    private WebElement tileViewButton;

    @FindBy(css = "div[class='input-group select-input customer-type']")
    private WebElement custTypeDropdown;

    @FindBy(css = "[aria-label='Search']")
    private WebElement custSearch;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public CustomerAdminPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navToolbar = new NavToolbar(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(newCustomerButton);
    }

    /**
     * Checks the customers button is present
     * @return true/false
     */
    public boolean isNewCustomerButtonPresent() {
        return pageUtils.waitForElementToAppear(newCustomerButton).isDisplayed();
    }
}
