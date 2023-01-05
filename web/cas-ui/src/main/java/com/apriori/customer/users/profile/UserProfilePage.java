package com.apriori.customer.users.profile;

import com.apriori.common.ModalUserList;
import com.apriori.customeradmin.NavToolbar;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.SelectComponent;
import com.apriori.utils.web.components.SourceListComponent;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Slf4j
public final class UserProfilePage extends NavToolbar {

    @FindBy(xpath = "//button[.='Edit']")
    private WebElement editButton;

    @FindBy(xpath = "//button[.='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//button[.='Save']")
    private WebElement saveButton;

    @FindBy(linkText = "< Back to User List Page")
    private WebElement backToUsersListPage;

    @FindBy(css = "[type='checkbox']")
    private WebElement statusCheckbox;

    @FindBy(css = "input[name='userProfile.givenName']")
    private WebElement givenNameInput;

    @FindBy(css = ".user-granted-access-controls")
    private WebElement grantedAccessControlsContainerRoot;
    private SourceListComponent grantedAccessControlsContainer;

    @FindBy(css = ".user-granted-licenses")
    private WebElement grantedLicensesContainerRoot;
    private SourceListComponent grantedLicensesContainer;

    @FindBy(xpath = "//button[.='Add']")
    private WebElement addApplicationButton;

    @FindBy(xpath = "//button[.='Remove']")
    private WebElement removeApplicationButton;

    @FindBy(css = ".site-drop-down")
    private WebElement siteDropDown;

    @FindBy(css = ".deployment-drop-down")
    private WebElement deploymentDropDown;

    @FindBy(xpath = "//button[@class='btn btn-primary'][.='OK']")
    private WebElement confirmRemoveOkButton;

    @FindBy(xpath = "//button[@class='mr-2 btn btn-secondary'][.='Cancel']")
    private WebElement confirmRemoveCancelButton;

    @FindBy(css = ".access-control-card")
    private WebElement aprioriCard;

    @FindBy(css = ".user-license-assignment-modify-button")
    private WebElement modifyLicenseButton;

    @FindBy(css = ".user-license-assignment")
    private WebElement licenseCard;

    @FindBy(css = "[role='dialog']")
    private WebElement selectLicenseDialog;

    @FindBy(css = ".license-drop-down")
    private WebElement licenseDropDown;
    private SelectComponent licenseSelectField;

    @FindBy(css = ".sub-license-drop-down")
    private WebElement subLicenseDropDown;
    private SelectComponent subLicenseSelectField;

    @FindBy(css = ".read-field-license")
    private WebElement licenseNameField;

    @FindBy(css = ".read-field-sub-license")
    private WebElement subLicenseNameField;

    @FindBy(css = ".user-granted-licenses .fa-arrows-rotate")
    private WebElement refreshLicenses;

    private ModalUserList modalUserList;

    public UserProfilePage(WebDriver driver) {
        super(driver);
        grantedAccessControlsContainer = new SourceListComponent(driver, grantedAccessControlsContainerRoot);
        grantedLicensesContainer = new SourceListComponent(driver, grantedLicensesContainerRoot);
        modalUserList = new ModalUserList(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementAppear(editButton);
    }

    /**
     * Edit customer info
     *
     * @return new page object
     */
    public UserProfilePage edit() {
        getPageUtils().waitForElementAndClick(editButton);
        return this;
    }

    /**
     * Cancels customer info
     *
     * @return new page object
     */
    public UserProfilePage cancel() {
        getPageUtils().waitForElementAndClick(cancelButton);
        return this;
    }

    /**
     * Saves user info
     *
     * @return new page object
     */
    public UserProfilePage save() {
        getPageUtils().waitForElementAndClick(saveButton);
        return this;
    }

    /**
     * Opens customer staff page
     *
     * @return new page object
     */
    public <T> T backToUsersListPage(Class<T> klass) {
        getPageUtils().waitForElementAndClick(backToUsersListPage);
        return PageFactory.initElements(getDriver(), klass);
    }

    /**
     * Changes status of user
     *
     * @return this object
     */
    public UserProfilePage changeStatus() {
        getPageUtils().waitForElementAndClick(statusCheckbox);
        return this;
    }

    /**
     * Gets user identity
     *
     * @return string
     */
    public String getUserIdentity() {
        return getDriver().findElement(By.xpath("//div[@class='text-overflow read-field read-field-identity']")).getAttribute("textContent");
    }

    /**
     * Checks if status checkbox is enabled
     *
     * @return true or false
     */
    public boolean isStatusCheckboxEditable() {
        return getPageUtils().isElementEnabled(statusCheckbox);
    }

    /**
     * Gets the label for the given name.
     *
     * @param name - name of field
     * @return The label for the given name.
     */
    public WebElement getReadOnlyLabel(String name) {
        return getPageUtils().waitForElementToAppear(By.cssSelector((String.format(".read-field-%s", name))));
    }

    /**
     * Gets the input for the given name.
     *
     * @param name - name of field
     * @return The input for the given name.
     */
    private WebElement getInput(String name) {
        if (name.equals("country-code") || name.equals("timezone")) {
            return getPageUtils().waitForElementToAppear(By.cssSelector(String.format(".select-field-user-profile-%s", name)));
        } else {
            return getPageUtils().waitForElementToAppear(By.xpath(String.format("//input[@name='%s']", name)));
        }
    }

    /**
     * Asserts that fields are read only
     *
     * @param namesOfFields - list of names of the fields
     * @param soft - soft assertions
     * @return - current page object
     */
    public UserProfilePage assertNonEditable(List<String> namesOfFields, SoftAssertions soft) {
        namesOfFields.forEach(name -> {
            soft.assertThat(getReadOnlyLabel(name))
                    .overridingErrorMessage(String.format("Expected field of %s to be read only.", name))
                    .isNotNull();
        });
        return this;
    }

    /**
     * Asserts that fields are editable
     *
     * @param nameOfFields - list of names of the fields
     * @param soft - soft assertions
     * @return - current page object
     */
    public UserProfilePage assertEditable(List<String> nameOfFields, SoftAssertions soft) {
        nameOfFields.forEach(name -> {
            soft.assertThat(getInput(name))
                    .overridingErrorMessage(String.format("Expected field of %s to be editable.", name))
                    .isNotNull();
        });
        return this;
    }

    /**
     * Checks if buttons are available
     *
     * @param soft - soft assertions
     * @param label - button label
     * @return - current page object
     */
    public UserProfilePage assertButtonAvailable(SoftAssertions soft, String label) {
        List<WebElement> elements = getDriver().findElements(By.xpath(String.format("//button[.='%s']", label)));
        soft.assertThat(elements.size())
                .overridingErrorMessage(String.format("Could not find the %s button", label))
                .isGreaterThan(0);
        return this;
    }

    /**
     * Checks if button is enabled
     *
     * @param button - name of button
     * @return - true or false
     */
    private boolean isButtonEnabled(WebElement button) {
        return button != null && button.isEnabled();
    }

    /**
     * Can click the save button.
     *
     * @return Boolean representing can click save button
     */
    public boolean canSave() {
        return isButtonEnabled(saveButton);
    }

    /**
     * Can click the edit button.
     *
     * @return Boolean representing can click edit button
     */
    public boolean canEdit() {
        return isButtonEnabled(editButton);
    }

    /**
     * Enter given name
     *
     * @param givenName - given name
     * @return current page object
     */
    public UserProfilePage editGivenName(String givenName) {
        getPageUtils().setValueOfElement(givenNameInput, givenName);
        return this;
    }

    /**
     * Gets the underlying access controls source list
     *
     * @return access controls source list
     */
    public SourceListComponent getGrantedAccessControlsContainer() {
        getPageUtils().waitForCondition(grantedAccessControlsContainer::isStable, PageUtils.DURATION_LOADING);
        return grantedAccessControlsContainer;
    }

    /**
     * Gets underlying granted licenses source list
     *
     * @return granted licenses source list
     */
    public SourceListComponent getGrantedLicensesContainer() {
        getPageUtils().waitForCondition(grantedLicensesContainer::isStable, PageUtils.DURATION_LOADING);
        return grantedLicensesContainer;
    }

    /**
     * Clicks on Add Application button
     *
     * @return this object
     */
    public UserProfilePage clickAddButton() {
        getPageUtils().waitForElementAndClick(addApplicationButton);
        return this;
    }

    /**
     * Clicks on Remove button
     *
     * @return this object
     */
    public UserProfilePage clickRemoveButton() {
        getPageUtils().waitForElementAndClick(removeApplicationButton);
        return this;
    }

    /**
     * Can click the remove button.
     *
     * @return Boolean representing can click remove button
     */
    public boolean canRemove() {
        return isButtonEnabled(removeApplicationButton);
    }

    /**
     * Validates that container has pagination and refresh button
     *
     * @param soft - soft assertions
     * @param container - type of container
     * @return this object
     */
    public UserProfilePage validateContainerIsPageableAndRefreshable(SoftAssertions soft, SourceListComponent container) {
        soft.assertThat(container.getPaginator())
            .overridingErrorMessage("The container is missing pagination.")
            .isNotNull();
        soft.assertThat(container.canRefresh())
            .overridingErrorMessage("The container is missing refresh button")
            .isTrue();

        return this;
    }

    /**
     * Clicks on Cancel button of candidates modal list
     *
     * @return this object
     */
    public UserProfilePage clickCancelModalButton() {
        return modalUserList.clickCandidatesCancelButton(UserProfilePage.class);
    }

    /**
     * Clicks on Close button of candidates modal list
     *
     * @return this object
     */
    public UserProfilePage clickCloseModalButton() {
        return modalUserList.clickCandidatesCloseButton(UserProfilePage.class);
    }

    /**
     * Clicks on Add button of candidates modal list
     *
     * @return this object
     */
    public UserProfilePage clickApplicationAddButton() {
        return modalUserList.clickCandidatesAddButton(UserProfilePage.class);
    }

    /**
     * Clicks on Cancel button of confirmation popup
     *
     * @return this object
     */
    public UserProfilePage clickModalConfirmationCancelButton() {
        return modalUserList.clickCandidatesConfirmCancelButton(UserProfilePage.class);
    }

    /**
     * Clicks on Close button of confirmation popup
     *
     * @return this object
     */
    public UserProfilePage clickModalConfirmationCloseButton() {
        return modalUserList.clickCandidatesConfirmCloseButton(UserProfilePage.class);
    }

    /**
     * Clicks on Ok button of confirmation popup
     *
     * @return this object
     */
    public UserProfilePage clickModalConfirmOkButton() {
        modalUserList.clickCandidatesConfirmOkButton(UserProfilePage.class);
        return this;
    }

    /**
     * Gets name of selected site in dropdown
     *
     * @return string name of selected site
     */
    public String getSiteInDropDown() {
        return siteDropDown.getAttribute("textContent");
    }

    /**
     * Gets name of selected deployment in dropdown
     *
     * @return string name of selected deployment
     */
    public String getDeploymentInDropDown() {
        return deploymentDropDown.getAttribute("textContent");
    }

    /**
     * Gets the underlying application candidates source list
     *
     * @return The application candidates source list
     */
    public SourceListComponent getApplicationCandidates() {
        return modalUserList.getCandidates();
    }

    /**
     * Clicks on Ok button of confirmation remove action
     *
     * @return this object
     */
    public UserProfilePage clickOkConfirmRemove() {
        getPageUtils().waitForElementAndClick(confirmRemoveOkButton);
        return this;
    }

    /**
     * Clicks on Cancel button of confirmation remove action
     *
     * @return this object
     */
    public UserProfilePage clickCancelConfirmRemove() {
        getPageUtils().waitForElementAndClick(confirmRemoveCancelButton);
        return this;
    }

    /**
     * Waits for card appearing after adding from modal
     *
     * @return this object
     */
    public UserProfilePage waitForCardIsDisplayed() {
        getPageUtils().waitForElementToAppear(aprioriCard);
        return this;
    }

    /**
     * Checks if access control card is displayed
     *
     * @return true or false
     */
    public boolean isCardDisplayed() {
        return getPageUtils().isElementDisplayed(aprioriCard);
    }

    /**
     * Clicks on Modify button
     *
     * @return this object
     */
    public UserProfilePage clickModifyLicenseButton() {
        getPageUtils().waitForElementAndClick(modifyLicenseButton);
        return this;
    }

    /**
     * Selects license from a dropdown
     *
     * @param license - name of license
     * @return this object
     */
    public UserProfilePage selectLicense(String license) {
        this.licenseSelectField = new SelectComponent(getDriver(), licenseDropDown);
        getPageUtils().waitForElementToAppear(licenseDropDown);
        licenseSelectField.select(license);
        return this;
    }

    /**
     * Selects sublicense from a dropdown
     *
     * @param subLicense - name of sublicense
     * @return this object
     */
    public UserProfilePage selectSubLicense(String subLicense) {
        this.subLicenseSelectField = new SelectComponent(getDriver(), subLicenseDropDown);
        getPageUtils().waitForElementToAppear(subLicenseDropDown);
        subLicenseSelectField.select(subLicense);
        return this;
    }

    /**
     * Clears license from a dropdown
     *
     * @return his object
     */
    public UserProfilePage deleteLicenseFromUser() {
        this.licenseSelectField = new SelectComponent(getDriver(), licenseDropDown);
        getPageUtils().waitForElementToAppear(licenseDropDown);
        licenseSelectField.clear();
        return this;
    }

    /**
     * Gets name of license from license card
     *
     * @return string
     */
    public String getLicenseName() {
        return getPageUtils().waitForElementToAppear(licenseNameField).getAttribute("textContent");
    }

    /**
     * Gets name of sublicense from license card
     *
     * @return string
     */
    public String getSubLicenseName() {
        return getPageUtils().waitForElementAppear(subLicenseNameField).getAttribute("textContent");
    }

    /**
     * Gets message for empty container
     *
     * @return string
     */
    public String getNoLicensesMessage() {
        return getPageUtils().waitForElementToAppear(By.cssSelector(".user-granted-licenses .message")).getAttribute("textContent");
    }

    public UserProfilePage refreshLicensesContainer() {
        getPageUtils().waitForElementAndClick(refreshLicenses);
        return this;
    }
}
