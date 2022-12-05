package com.apriori.customer.users;

import com.apriori.common.ModalUserList;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.SourceListComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the page for aPriori Staff under the users tab.
 */
@Slf4j
public final class StaffPage extends UsersPage {
    @FindBy(className = "customer-user-associations-add-button")
    private WebElement addButton;

    @FindBy(className = "customer-user-associations-remove-button")
    private WebElement removeButton;

    @FindBy(xpath = "//button[@class='btn btn-primary'][.='OK']")
    private WebElement confirmRemoveOkButton;

    @FindBy(xpath = "//button[@class='mr-2 btn btn-secondary'][.='Cancel']")
    private WebElement confirmRemoveCancelButton;

    @FindBy(className = "user-list-apriori-staff")
    private WebElement staffAssociationListViewRoot;
    private SourceListComponent staffAssociationList;

    private ModalUserList modalUserList;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver that the page exists on.
     */
    public StaffPage(WebDriver driver) {
        super(driver);
        staffAssociationList = new SourceListComponent(driver, staffAssociationListViewRoot);
        modalUserList = new ModalUserList(driver);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(addButton);
        getPageUtils().waitForElementToAppear(removeButton);
        getPageUtils().waitForElementToAppear(staffAssociationListViewRoot);
    }

    /**
     * Gets the staff association list.
     *
     * @return The staff association list.
     */
    public SourceListComponent getStaffAssociationList() {
        getPageUtils().waitForCondition(staffAssociationList::isStable, PageUtils.DURATION_LOADING);
        return staffAssociationList;
    }

    /**
     * Clicks the provided button and waits for the staff association list to reload.
     *
     * @param button The button to click.
     * @return This object.
     * @throws org.openqa.selenium.ElementNotInteractableException If the button is disabled.
     */
    private StaffPage clickAndWait(final WebElement button) {
        getPageUtils().waitForElementAndClick(button);
        getPageUtils().waitForCondition(staffAssociationList::isStable, PageUtils.DURATION_LOADING);
        return this;
    }

    /**
     * Clicks the 'Remove from list' button.
     *
     * @return This object.
     * @throws org.openqa.selenium.ElementNotInteractableException If the button is disabled.
     */
    public StaffPage clickRemoveButton() {
        getPageUtils().waitForElementAndClick(removeButton);
        return this;
    }

    /**
     * Clicks the Add from List button.
     *
     * @return The opened modal.
     */
    public StaffPage clickAddFromList() {
        getPageUtils().waitForElementAndClick(addButton);
        return this;
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
     * Clicks on Cancel button of candidates modal list
     *
     * @return this object
     */
    public StaffPage clickCandidatesCancelButton() {
        return modalUserList.clickCandidatesCancelButton(StaffPage.class);
    }

    /**
     * Clicks on Close button of candidates modal list
     *
     * @return this object
     */
    public StaffPage clickCandidatesCloseButton() {
        return modalUserList.clickCandidatesCloseButton(StaffPage.class);
    }

    /**
     * Clicks on Add button of candidates modal list
     *
     * @return this object
     */
    public StaffPage clickCandidatesAddButton() {
        return modalUserList.clickCandidatesAddButton(StaffPage.class);
    }

    /**
     * Clicks on Cancel button of candidates confirm dialog
     *
     * @return this object
     */
    public StaffPage clickCandidatesConfirmCancelButton() {
        return modalUserList.clickCandidatesConfirmCancelButton(StaffPage.class);
    }

    /**
     * Clicks on Ok button of candidates confirm dialog
     *
     * @return this object
     */
    public StaffPage clickCandidatesConfirmOkButton() {
        return modalUserList.clickCandidatesConfirmOkButton(StaffPage.class);
    }

    /**
     * Clicks on Close button of candidates confirm dialog
     *
     * @return this object
     */
    public StaffPage clickCandidatesConfirmCloseButton() {
        return modalUserList.clickCandidatesConfirmCloseButton(StaffPage.class);
    }

    /**
     * Clicks on OK button of remove confirm dialog
     *
     * @return this object
     */
    public StaffPage clickConfirmRemoveOkButton() {
        return clickAndWait(confirmRemoveOkButton);
    }

    /**
     * Clicks on Cncel button of remove confirm dialog
     *
     * @return
     */
    public StaffPage clickConfirmRemoveCancelButton() {
        getPageUtils().waitForElementAndClick(confirmRemoveCancelButton);
        return this;
    }
}
