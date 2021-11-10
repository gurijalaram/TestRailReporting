package com.apriori.newcustomer;

import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.SelectFieldComponent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerProfilePage extends EagerPageComponent<CustomerProfilePage> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerProfilePage.class);

    @FindBy(className = "customer-profile")
    private WebElement root;

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


    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver used to query the entire page.
     */
    public CustomerProfilePage(WebDriver driver) {
        super(driver, logger);
        this.customerTypeSelectField = new SelectFieldComponent(driver, customerTypeDropdown);
    }

    /**
     * @inheritDoc
     *
     * @throws Error If the profile tab or users tab is missing
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(root);
    }

    /**
     * Gets a value that indicates if the cloud reference input is enabled.
     *
     * @return A value that indicates if the cloud reference input is enabled
     */
    public boolean isCloudReferenceEnabled() {
        return getPageUtils().isElementEnabled(cloudRefInput);
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
        getPageUtils().setValueOfElement(customerNameInput, customerName);
        return this;
    }

    /**
     * Enter description
     *
     * @param description - description
     * @return current page object
     */
    public CustomerProfilePage enterDescription(String description) {
        getPageUtils().setValueOfElement(descriptionInput, description);
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
        getPageUtils().setValueOfElement(salesforceInput, salesforceId);
        return this;
    }

    /**
     * Enter cloud info
     *
     * @param cloudReference - cloud ref
     * @return current page object
     */
    public CustomerProfilePage enterCloudRef(String cloudReference) {
        getPageUtils().setValueOfElement(cloudRefInput, cloudReference);
        return this;
    }

    /**
     * Enter email domain info
     *
     * @param emailDomains - email
     * @return current page object
     */
    public CustomerProfilePage enterEmailDomains(String emailDomains) {
        getPageUtils().setValueOfElement(emailDomInput, emailDomains);
        return this;
    }

    /**
     * Enter the maximum cad file retention days.
     *
     * @param days Max Cad file retention policy in days
     * @return current page object
     */
    public CustomerProfilePage enterCadFileRetentionPolicy(String days) {
        getPageUtils().setValueOfElement(cadFileRetentionDaysInput, days);
        return this;
    }

    /**
     * Enter the maximum cad file size.
     *
     * @param size Max cad file size
     * @return current page object
     */
    public CustomerProfilePage enterMaxCadFileSize(String size) {
        getPageUtils().setValueOfElement(maxCadFileSizeInput, size);
        return this;
    }

    /**
     * Edit customer info
     *
     * @return current page object
     */
    public CustomerProfilePage clickEditButton() {
        getPageUtils().waitForElementAndClick(editButton);
        return this;
    }

    /**
     * Cancel customer info
     *
     * @return new page object
     */
    public <T> T clickCancelButton(Class<T> klass) {
        getPageUtils().waitForElementAndClick(cancelButton);
        return PageFactory.initElements(getDriver(), klass);
    }

    public boolean isSaveButtonEnabled() {
        return getPageUtils().isElementEnabled(saveButton);
    }

    /**
     * Save customer info
     *
     * @return new page object
     */
    public CustomerProfilePage clickSaveButton() {
        getPageUtils().waitForElementAndClick(saveButton);
        return new CustomerProfilePage(getDriver());
    }
}
