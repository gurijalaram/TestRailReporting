package com.apriori.newcustomer;

import com.apriori.newcustomer.users.UsersListPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.components.SelectFieldComponent;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CustomerProfilePage extends LoadableComponent<CustomerProfilePage> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerProfilePage.class);

    @FindBy(xpath = "//a[.='Profile']")
    private WebElement profileTab;

    @FindBy(xpath = "//a[.='Users']")
    private WebElement usersTab;

    @FindBy(xpath = "//a[.='Sites & Licenses']")
    private WebElement siteLicenseTab;

    @FindBy(xpath = "//a[.='Infrastructure']")
    private WebElement infraStructTab;

    @FindBy(xpath = "//a[.='Security']")
    private WebElement securityTab;

    @FindBy(xpath = "//a[.='System Configuration']")
    private WebElement systemConfigurationTab;

    @FindBy(css = ".edit-read-form button.edit-button")
    private WebElement editButton;

    @FindBy(css = ".edit-read-form button[type='submit']")
    private WebElement saveButton;

    @FindBy(css = ".edit-read-form button.btn-secondary")
    private WebElement cancelButton;

    @FindBy(css = "input[name='name']")
    private WebElement customerNameInput;

    @FindBy(className = "invalid-feedback-for-name")
    private WebElement customerNameFeedback;

    @FindBy(css = "input[name='description']")
    private WebElement descriptionInput;

    @FindBy(className = "invalid-feedback-for-description")
    private WebElement descriptionFeedback;

    @FindBy(className = "select-field-customer-type")
    private WebElement customerTypeDropdown;
    private SelectFieldComponent customerTypeSelectField;

    @FindBy(className = "invalid-feedback-for-customer-type")
    private WebElement customerTypeFeedback;

    @FindBy(css = "input[name='salesforceId']")
    private WebElement salesforceInput;

    @FindBy(className = "invalid-feedback-for-salesforce-id")
    private WebElement salesforceFeedback;

    @FindBy(css = "input[name='cloudReference']")
    private WebElement cloudRefInput;

    @FindBy(className = "invalid-feedback-for-cloud-reference")
    private WebElement cloudRefFeedback;

    @FindBy(css = "input[name='emailDomains']")
    private WebElement emailDomInput;

    @FindBy(className = "invalid-feedback-for-email-domains")
    private WebElement emailDomFeedback;

    @FindBy(css = "input[name='maxCadFileRetentionDays']")
    private WebElement cadFileRetentionDaysInput;

    @FindBy(className = "invalid-feedback-for-max-cad-file-retention-days")
    private WebElement cadFileRetentionDaysFeedback;

    @FindBy(css = "input[name='maxCadFileSize']")
    private WebElement maxCadFileSizeInput;

    @FindBy(className = "invalid-feedback-for-max-cad-file-size")
    private WebElement maxCadFileSizeFeedback;

    private WebDriver driver;
    private PageUtils pageUtils;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver used to query the entire page.
     */
    public CustomerProfilePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);

        this.customerTypeSelectField = new SelectFieldComponent(driver, customerTypeDropdown);
        this.get();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void load() {
    }

    /**
     * @inheritDoc
     *
     * @throws Error If the profile tab or users tab is missing
     */
    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(profileTab);
        pageUtils.waitForElementAppear(usersTab);
    }

    /**
     * Gets a value that indicates if the profile tab is displayed.
     *
     * @return True if the profile tab is displayed.
     */
    public boolean isProfileTabDisplayed() {
        return profileTab.isDisplayed();
    }

    /**
     * Gets a value that indicates if the users tab is enabled and can be clicked.
     *
     * @return A value that indicates if the users tab is enabled
     */
    public boolean isUsersTabEnabled() {
        return !StringUtils.equalsIgnoreCase("true", usersTab.getAttribute("disabled"));
    }

    /**
     * Gets a value that indicates if the sites & licenses tab is enabled and can be clicked.
     *
     * @return A value that indicates if the sites & licenses tab is enabled
     */
    public boolean isSitesAndLicensesEnabled() {
        return !StringUtils.equalsIgnoreCase("true", siteLicenseTab.getAttribute("disabled"));
    }

    /**
     * Gets a value that indicates if the infrastructure tab is enabled and can be clicked.
     *
     * @return A value that indicates if the infrastructure tab is enabled
     */
    public boolean isInfrastructureEnabled() {
        return !StringUtils.equalsIgnoreCase("true", infraStructTab.getAttribute("disabled"));
    }

    /**
     * Gets a value that indicates if the security tab is enabled and can be clicked.
     *
     * @return A value that indicates if the security tab is enabled
     */
    public boolean isSecurityEnabled() {
        return !StringUtils.equalsIgnoreCase("true", securityTab.getAttribute("disabled"));
    }

    /**
     * Gets a value that indicates if the system configuration tab is enabled and can be clicked.
     *
     * @return A value that indicates if the system configuration tab is enabled
     */
    public boolean isSystemConfigurationEnabled() {
        return !StringUtils.equalsIgnoreCase("true", systemConfigurationTab.getAttribute("disabled"));
    }

    /**
     * Gets a value that indicates if the cloud reference input is enabled.
     *
     * @return A value that indicates if the cloud reference input is enabled
     */
    public boolean isCloudReferenceEnabled() {
        return pageUtils.isElementEnabled(cloudRefInput);
    }

    /**
     * Gets the current validation feedback for the customer name.
     *
     * @return The current validation error for the customer name.  Returns the empty string
     *         if the value is valid.
     */
    public String getCustomerNameFeedback() {
        return this.customerNameFeedback.getText();
    }

    /**
     * Gets the current validation feedback for the description.
     *
     * @return The current validation error for the description.  Returns the empty string
     *         if the value is valid.
     */
    public String getDescriptionFeedback() {
        return this.descriptionFeedback.getText();
    }

    /**
     * Gets the current validation feedback for the customer type.
     *
     * @return The current validation error for the customer type.  Returns the empty string
     *         if the value is valid.
     */
    public String getCustomerTypeFeedback() {
        return this.customerTypeFeedback.getText();
    }

    /**
     * Gets the current validation feedback for the salesforce ID.
     *
     * @return The current validation error for the salesforce ID.  Returns the empty string
     *         if the value is valid.
     */
    public String getSalesforceIdFeedback() {
        return this.salesforceFeedback.getText();
    }

    /**
     * Gets the value of the cloud ref field.
     *
     * @return The text value of the cloud ref field.
     */
    public String getCloudRefValue() {
        return cloudRefInput.getAttribute("value");
    }

    /**
     * Gets the current validation feedback for the cloud reference.
     *
     * @return The current validation error for the cloud reference.  Returns the empty string
     *         if the value is valid.
     */
    public String getCloudRefFeedback() {
        return this.cloudRefFeedback.getText();
    }

    /**
     * Gets the current validation feedback for the email domains.
     *
     * @return The current validation error for the email domains.  Returns the empty string
     *         if the value is valid.
     */
    public String getEmailDomFeedback() {
        return this.emailDomFeedback.getText();
    }

    /**
     * Gets the current validation feedback for the cad file retention policy.
     *
     * @return The current validation error for the cad file retention policy.  Returns the empty string
     *         if the value is valid.
     */
    public String getCadFileRetentionPolicyFeedback() {
        return this.cadFileRetentionDaysFeedback.getText();
    }

    /**
     * Gets the current validation feedback for the max cad file size.
     *
     * @return The current validation error for the max cad file size.  Returns the empty string
     *         if the value is valid.
     */
    public String getMaxCadFileSizeFeedback() {
        return this.maxCadFileSizeFeedback.getText();
    }

    /**
     * Enter customer name
     *
     * @param customerName - customer name
     * @return current page object
     */
    public CustomerProfilePage enterCustomerName(String customerName) {
        pageUtils.setValueOfElement(customerNameInput, customerName);
        return this;
    }

    /**
     * Enter description
     *
     * @param description - description
     * @return current page object
     */
    public CustomerProfilePage enterDescription(String description) {
        pageUtils.setValueOfElement(descriptionInput, description);
        return this;
    }

    /**
     * Enter customer type
     *
     * @param customerType - customer type
     * @return current page object
     */
    private CustomerProfilePage selectCustomerType(String customerType) {
        customerTypeSelectField.getSelect().select(customerType);
        return this;
    }

    /**
     * Clears the customer type.
     *
     * @return This object
     */
    public CustomerProfilePage clearCustomerType() {
        return selectCustomerType(null);
    }

    /**
     * Selects the customer type, "On Premise"
     *
     * @return This object.
     */
    public CustomerProfilePage selectCustomerTypeOnPremise() {
        return selectCustomerType("On Premise");
    }

    /**
     * Selects the customer type, "Cloud"
     *
     * @return This object.
     */
    public CustomerProfilePage selectCustomerTypeCloud() {
        return selectCustomerType("Cloud");
    }

    /**
     * Selects the customer type, "Cloud & On Premise"
     *
     * @return This object.
     */
    public CustomerProfilePage selectCustomerTypeOnPremiseAndCloud() {
        return selectCustomerType("Cloud & On Premise");
    }

    /**
     * Enter sales force info
     *
     * @param salesforceId - sales force id
     * @return current page object
     */
    public CustomerProfilePage enterSalesforceId(String salesforceId) {
        pageUtils.setValueOfElement(salesforceInput, salesforceId);
        return this;
    }

    /**
     * Enter cloud info
     *
     * @param cloudReference - cloud ref
     * @return current page object
     */
    public CustomerProfilePage enterCloudRef(String cloudReference) {
        pageUtils.setValueOfElement(cloudRefInput, cloudReference);
        return this;
    }

    /**
     * Enter email domain info
     *
     * @param emailDomains - email
     * @return current page object
     */
    public CustomerProfilePage enterEmailDomains(String emailDomains) {
        pageUtils.setValueOfElement(emailDomInput, emailDomains);
        return this;
    }

    /**
     * Enter the maximum cad file retention days.
     *
     * @param days Max Cad file retention policy in days
     * @return current page object
     */
    public CustomerProfilePage enterCadFileRetentionPolicy(String days) {
        pageUtils.setValueOfElement(cadFileRetentionDaysInput, days);
        return this;
    }

    /**
     * Enter the maximum cad file size.
     *
     * @param size Max cad file size
     * @return current page object
     */
    public CustomerProfilePage enterMaxCadFileSize(String size) {
        pageUtils.setValueOfElement(maxCadFileSizeInput, size);
        return this;
    }

    /**
     * Attempts to discover the customer identity.
     *
     * @return The customer identity for an existing customer, "new" for a new customer,
     *         and the empty string if it cannot determine the identity.
     */
    public String findCustomerIdentity() {
        String baseUrl = PropertiesContext.get("${env}.cas.ui_url");
        String url = driver.getCurrentUrl().replace(String.format("%s/customers/", baseUrl), "");
        return Arrays.stream(url.split("/")).findFirst().orElse("");
    }

    /**
     * Go to users tab
     *
     * @return new page object
     */
    public UsersListPage goToUsersList() {
        pageUtils.waitForElementAndClick(usersTab);
        return new UsersListPage(driver);
    }

    /**
     * Go to sites and licenses tab
     *
     * @return new page object
     */
    public SitesLicensesPage goToSitesLicenses() {
        pageUtils.waitForElementAndClick(siteLicenseTab);
        return new SitesLicensesPage(driver);
    }

    /**
     * Go to infrastructure tab
     *
     * @return new page object
     */
    public InfrastructurePage goToInfrastructure() {
        pageUtils.waitForElementAndClick(infraStructTab);
        return new InfrastructurePage(driver);
    }

    /**
     * Edit customer info
     *
     * @return current page object
     */
    public CustomerProfilePage clickEditButton() {
        pageUtils.waitForElementAndClick(editButton);
        return this;
    }

    /**
     * Cancel customer info
     *
     * @return new page object
     */
    public <T> T clickCancelButton(Class<T> klass) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, klass);
    }

    public boolean isSaveButtonEnabled() {
        return pageUtils.isElementEnabled(saveButton);
    }

    /**
     * Save customer info
     *
     * @return new page object
     */
    public CustomerProfilePage clickSaveButton() {
        pageUtils.waitForElementAndClick(saveButton);
        return new CustomerProfilePage(driver);
    }
}
