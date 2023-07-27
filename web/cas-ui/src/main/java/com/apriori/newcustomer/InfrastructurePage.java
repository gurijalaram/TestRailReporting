package com.apriori.newcustomer;

import com.apriori.common.ModalUserList;
import com.apriori.components.SourceListComponent;
import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.utils.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class InfrastructurePage extends CustomerWorkspacePage {

    @FindBy(className = "infrastructure-workspace")
    private WebElement infrastructureWorkspace;

    @FindBy(xpath = "//div[contains(text(),'Please select infrastructure within the Tree View')]")
    private WebElement noContentMessage;

    @FindBy(className = "application-grant-all-button")
    private WebElement grantAllButton;

    @FindBy(className = "application-deny-all-button")
    private WebElement denyAllButton;

    @FindBy(css = "[data-testid='primary-button']")
    private WebElement confirmAllOkButton;

    @FindBy(css = "[data-testid='secondary-button']")
    private WebElement confirmAllCancelButton;

    @FindBy(css = ".Toastify__toast.Toastify__toast--success .Toastify__toast-body")
    private WebElement successMessage;

    @FindBy(css = ".Toastify__close-button.Toastify__close-button--success")
    private WebElement closeMessage;

    private ModalUserList modalUserList;

    public InfrastructurePage(WebDriver driver) {
        super(driver);
        modalUserList = new ModalUserList(driver);
    }

    /**
     * Retrieves CustomerProfilePage for customer via URL and returns Page object.
     *
     * @param driver   - WebDriver
     * @param customer - Customer ID
     * @return InfrastructurePage
     */
    public static InfrastructurePage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("cas.ui_url") + "customers/%s/infrastructure";
        driver.navigate().to(String.format(url, customer));
        return new InfrastructurePage(driver);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementAppear(infrastructureWorkspace);
    }

    /**
     * Select the infrastructure dropdown
     *
     * @param infrastructures - the infrastructure
     * @return current page object
     */
    public InfrastructurePage selectInfrastructureDropdown(String infrastructures) {
        String[] listInfrastructure = infrastructures.split(",");

        for (String infrastructure : listInfrastructure) {
            By dropdown = By.xpath(String.format("//li[.='%s']//div[@class='rstm-toggle-icon-symbol']", infrastructure));
            getPageUtils().waitForElementAndClick(dropdown);
        }
        return this;
    }

    /**
     * Select the application
     *
     * @param application - the name
     * @return current page object
     */
    public InfrastructurePage selectApplication(String application) {
        By data = By.xpath(String.format("//li[.='%s']", application));
        getPageUtils().waitForElementAndClick(data);
        return this;
    }

    /**
     * Get the data for each field
     *
     * @param field - the field
     * @return string
     */
    public String getApplicationDetails(String field) {
        By fieldName = By.xpath(String.format("//div[contains(text(),'%s')]/ancestor::div[@class='py-2 ']", field));
        return getPageUtils().waitForElementToAppear(fieldName).getText();
    }

    /**
     * Clicks on Grant All button
     *
     * @return this object
     */
    public InfrastructurePage clickGrantAllButton() {
        getPageUtils().waitForElementAndClick(grantAllButton);
        return this;
    }

    /**
     * Clicks on Deny All button
     *
     * @return this object
     */
    public InfrastructurePage clickDenyAllButton() {
        getPageUtils().waitForElementAndClick(denyAllButton);
        return this;
    }

    /**
     * Clicks on Cancel button of candidates modal list
     *
     * @return this object
     */
    public InfrastructurePage clickCandidatesCancelButton() {
        return modalUserList.clickCandidatesCancelButton(InfrastructurePage.class);
    }

    /**
     * Clicks on Close button of candidates modal list
     *
     * @return this object
     */
    public InfrastructurePage clickCandidatesCloseButton() {
        return modalUserList.clickCandidatesCloseButton(InfrastructurePage.class);
    }

    /**
     * Clicks on Add button of candidates modal list
     *
     * @return this object
     */
    public InfrastructurePage clickCandidatesAddButton() {
        return modalUserList.clickCandidatesAddButton(InfrastructurePage.class);
    }

    /**
     * Clicks on Cancel button of candidates confirm dialog
     *
     * @return this object
     */
    public InfrastructurePage clickCandidatesConfirmCancelButton() {
        return modalUserList.clickCandidatesConfirmCancelButton(InfrastructurePage.class);
    }

    /**
     * Clicks on Ok button of candidates confirm dialog
     *
     * @return this object
     */
    public InfrastructurePage clickCandidatesConfirmOkButton() {
        return modalUserList.clickCandidatesConfirmOkButton(InfrastructurePage.class);
    }

    /**
     * Clicks on Close button of candidates confirm dialog
     *
     * @return this object
     */
    public InfrastructurePage clickCandidatesConfirmCloseButton() {
        return modalUserList.clickCandidatesConfirmCloseButton(InfrastructurePage.class);
    }

    /**
     * Gets the underlying user candidates source list.
     *
     * @return The user candidates source list.
     */
    public SourceListComponent getCandidates() {
        return modalUserList.getCandidates();
    }

    /**
     * Clicks on Cancel button of candidates confirm dialog on Application page
     *
     * @return this object
     */
    public InfrastructurePage clickAllCancelButton() {
        getPageUtils().waitForElementAndClick(confirmAllCancelButton);
        return this;
    }

    /**
     * Clicks on Ok button of candidates confirm dialog on Application page
     *
     * @return this object
     */
    public InfrastructurePage clickAllOkButton() {
        getPageUtils().waitForElementAndClick(confirmAllOkButton);
        return this;
    }

    /**
     * Gets success message text
     *
     * @return string success message
     */
    public String getTextSuccessMessage() {
        return getPageUtils().waitForElementToAppear(successMessage).getAttribute(("textContent"));
    }

    /**
     * Closes modal message
     *
     * @return this page object
     */
    public InfrastructurePage closeMessage() {
        getPageUtils().waitForElementAndClick(closeMessage);
        return this;
    }
}
