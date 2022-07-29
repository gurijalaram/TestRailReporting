package com.apriori.newcustomer;

import com.apriori.common.ModalUserList;
import com.apriori.customeradmin.NavToolbar;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.components.SourceListComponent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfrastructurePage extends LoadableComponent<InfrastructurePage> {

    private static final Logger logger = LoggerFactory.getLogger(InfrastructurePage.class);

    @FindBy(className = "infrastructure-workspace")
    private WebElement infrastructureWorkspace;

    @FindBy(xpath = "//div[contains(text(),'Please select infrastructure within the Tree View')]")
    private WebElement noContentMessage;

    @FindBy(className = "application-grant-all-button")
    private WebElement grantAllButton;

    @FindBy(className = "application-deny-all-button")
    private WebElement denyAllButton;

    @FindBy(css = ".btn.btn-primary")
    private WebElement confirmAllOkButton;

    @FindBy(css = ".btn.btn-secondary.mr-2")
    private WebElement confirmAllCancelButton;

    @FindBy(css = ".Toastify__toast.Toastify__toast--success .Toastify__toast-body")
    private WebElement successMessage;

    @FindBy(css = ".Toastify__close-button.Toastify__close-button--success")
    private WebElement closeMessage;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;
    private ModalUserList modalUserList;

    public InfrastructurePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navToolbar = new NavToolbar(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
        modalUserList = new ModalUserList(driver);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(infrastructureWorkspace);
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
            pageUtils.waitForElementAndClick(dropdown);
        }
        return this;
    }

    /**
     * Select the application
     * @param application - the name
     * @return current page object
     */
    public InfrastructurePage selectApplication(String application) {
        By data = By.xpath(String.format("//li[.='%s']", application));
        pageUtils.waitForElementAndClick(data);
        return this;
    }

    /**
     * Get the data for each field
     * @param field - the field
     * @return string
     */
    public String getApplicationDetails(String field) {
        By fieldName = By.xpath(String.format("//div[contains(text(),'%s')]/ancestor::div[@class='py-2 ']", field));
        return pageUtils.waitForElementToAppear(fieldName).getText();
    }

    /**
     * Retrieves CustomerProfilePage for customer via URL and returns Page object.
     *
     * @param driver - WebDriver
     * @param customer - Customer ID
     * @return InfrastructurePage
     */
    public static InfrastructurePage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("${env}.cas.ui_url") + "customers/%s/infrastructure";
        driver.navigate().to(String.format(url, customer));
        return new InfrastructurePage(driver);
    }

    /**
     * Clicks on Grant All button
     *
     * @return this object
     */
    public InfrastructurePage clickGrantAllButton() {
        pageUtils.waitForElementAndClick(grantAllButton);
        return this;
    }

    /**
     * Clicks on Deny All button
     *
     * @return this object
     */
    public InfrastructurePage clickDenyAllButton() {
        pageUtils.waitForElementAndClick(denyAllButton);
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
        pageUtils.waitForElementAndClick(confirmAllCancelButton);
        return this;
    }

    /**
     * Clicks on Ok button of candidates confirm dialog on Application page
     *
     * @return this object
     */
    public InfrastructurePage clickAllOkButton() {
        pageUtils.waitForElementAndClick(confirmAllOkButton);
        return this;
    }

    /**
     * Gets success message text
     *
     * @return string success message
     */
    public String getTextSuccessMessage() {
        return pageUtils.waitForElementToAppear(successMessage).getAttribute(("textContent"));
    }

    /**
     * Closes modal message
     *
     * @return this page object
     */
    public InfrastructurePage closeMessage() {
        closeMessage.click();
        return this;
    }
}
