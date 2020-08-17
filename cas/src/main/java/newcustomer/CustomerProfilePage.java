package newcustomer;

import com.apriori.utils.PageUtils;

import customeradmin.CustomerAdminPage;
import customeradmin.NavToolbar;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerProfilePage extends LoadableComponent<CustomerProfilePage> {

    private final Logger logger = LoggerFactory.getLogger(CustomerProfilePage.class);

    @FindBy(xpath = "//a[.='Profile']")
    private WebElement profileTab;

    @FindBy(xpath = "//a[.='Users']")
    private WebElement usersTab;

    @FindBy(xpath = "//a[.='Site & License']")
    private WebElement siteLicenseTab;

    @FindBy(xpath = "//a[.='Infrastructure']")
    private WebElement infraStructTab;

    @FindBy(xpath = "//a[.='Security']")
    private WebElement securityTab;

    @FindBy(xpath = "//div[@class='d-flex align-items-center']//button[.='Edit']")
    private WebElement editButton;

    @FindBy(xpath = "//div[@class='d-flex align-items-center']//button[.='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//div[@class='d-flex align-items-center']//button[.='Cancel']")
    private WebElement cancelButton;

    @FindBy(css = "input[name='name']")
    private WebElement customerNameInput;

    @FindBy(css = "input[name='description']")
    private WebElement descriptionInput;

    @FindBy(css = "select[name='customerType']")
    private WebElement customerTypeDropdown;

    @FindBy(css = "input[name='salesforceId']")
    private WebElement salesforceInput;

    @FindBy(css = "input[name='cloudReference']")
    private WebElement cloudRefInput;

    @FindBy(css = "input[name='emailDomains']")
    private WebElement emailDomInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public CustomerProfilePage(WebDriver driver) {
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
        pageUtils.waitForElementAppear(profileTab);
        pageUtils.waitForElementAppear(usersTab);
    }

    /**
     * Enter customer name
     * @param customerName - customer name
     * @return current page object
     */
    public CustomerProfilePage enterCustomerName(String customerName) {
        pageUtils.waitForElementToAppear(customerNameInput).clear();
        customerNameInput.sendKeys(customerName);
        return this;
    }

    /**
     * Enter description
     * @param description - description
     * @return current page object
     */
    public CustomerProfilePage enterDescription(String description) {
        pageUtils.waitForElementToAppear(descriptionInput).clear();
        descriptionInput.sendKeys(description);
        return this;
    }

    /**
     * Enter customer type
     * @param customerType - customer type
     * @return current page object
     */
    public CustomerProfilePage selectCustomerType(String customerType) {
        pageUtils.selectDropdownOption(customerTypeDropdown, customerType);
        return this;
    }

    /**
     * Enter sales force info
     * @param salesforceid - sales force id
     * @return current page object
     */
    public CustomerProfilePage enterSalesforceId(String salesforceid) {
        pageUtils.waitForElementToAppear(salesforceInput).clear();
        salesforceInput.sendKeys(salesforceid);
        return this;
    }

    /**
     * Enter cloud info
     * @param cloudref - cloud ref
     * @return current page object
     */
    public CustomerProfilePage enterCloudRef(String cloudref) {
        pageUtils.waitForElementToAppear(cloudRefInput).clear();
        cloudRefInput.sendKeys(cloudref);
        return this;
    }

    /**
     * Enter email domain info
     * @param emailDomains - email
     * @return current page object
     */
    public CustomerProfilePage enterEmailDomains(String emailDomains) {
        pageUtils.waitForElementToAppear(emailDomInput).clear();
        emailDomInput.sendKeys(emailDomains);
        return this;
    }

    /**
     * Edit customer info
     * @return current page object
     */
    public CustomerProfilePage edit() {
        pageUtils.waitForElementAndClick(editButton);
        return this;
    }

    /**
     * Cancels customer info
     * @return new page object
     */
    public <T> T cancel(Class<T> klass) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Saves customer info
     * @return new page object
     */
    public CustomerAdminPage save() {
        pageUtils.waitForElementAndClick(saveButton);
        return new CustomerAdminPage(driver);
    }
}
