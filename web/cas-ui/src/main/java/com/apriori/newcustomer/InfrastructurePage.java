package com.apriori.newcustomer;

import com.apriori.common.ModalUserList;
import com.apriori.customeradmin.NavToolbar;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;

import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.TableComponent;
import com.apriori.utils.web.components.TableHeaderComponent;

import org.assertj.core.api.SoftAssertions;
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

    @FindBy(css = ".apriori-source-list-layout-table")
    private WebElement applicationAccessControlsTableRoot;
    private final SourceListComponent applicationAccessControlsTable;

    @FindBy(className = "application-grant-button")
    private WebElement grantFromListButton;

    @FindBy(className = "application-deny-button")
    private WebElement denySelectedButton;

    @FindBy(className = "application-grant-all-button")
    private WebElement grantAllButton;

    @FindBy(className = "application-deny-all-button")
    private WebElement denyAllButton;

    @FindBy(css = ".btn.btn-primary")
    private WebElement confirmAllOkButton;

    @FindBy(css = ".btn.btn-secondary.mr-2")
    private WebElement confirmAllCancelButton;

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
        applicationAccessControlsTable = new SourceListComponent(driver, applicationAccessControlsTableRoot);
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
     * Gets the access controls table
     *
     * @return application access controls table
     */
    public SourceListComponent getApplicationAccessControlsTable() {
        pageUtils.waitForCondition(applicationAccessControlsTable::isStable, pageUtils.DURATION_LOADING);
        return applicationAccessControlsTable;
    }

    /**
     * Validates that table has a correct columns
     *
     * @param expectedName name of column
     * @param id id of column
     * @param soft soft assertions
     * @return This object
     */
    public InfrastructurePage validateAccessControlsTableHasCorrectColumns(String expectedName, String id, SoftAssertions soft) {
        SourceListComponent list = applicationAccessControlsTable;
        TableComponent table = Obligation.mandatory(list::getTable, "The access controls table is missing");

        TableHeaderComponent header = table.getHeader(id);
        soft.assertThat(header)
                .overridingErrorMessage("The '%s' column is missing.", expectedName)
                .isNotNull();

        if (header != null) {
            String name = header.getName();
            soft.assertThat(name)
                    .overridingErrorMessage("The '%s' column is incorrectly named '%s'", expectedName, name)
                    .isEqualTo(expectedName);
            soft.assertThat(header.canSort())
                    .overridingErrorMessage("The '%s' column is not sortable.")
                    .isTrue();
        }
        return this;
    }

    /**
     * Clicks on Grant From List button
     *
     * @return this object
     */
    public InfrastructurePage clickGrantFromList() {
        pageUtils.waitForElementAndClick(grantFromListButton);
        return this;
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
     * Clicks on Deny Selected button
     *
     * @return this object
     */
    public InfrastructurePage clickDenySelectedButton() {
        pageUtils.waitForElementAndClick(denySelectedButton);
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
}
