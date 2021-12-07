package com.apriori.customer.users;

import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.SourceListComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the page for aPriori Staff under the users tab.
 */
@Slf4j
public final class StaffPage extends EagerPageComponent<StaffPage> {
    @FindBy(className = "staff-association-add-button")
    private WebElement addButton;

    @FindBy(className = "staff-association-disable-button")
    private WebElement disableButton;

    @FindBy(className = "staff-association-enable-button")
    private WebElement enableButton;

    @FindBy(className = "staff-association-list-view")
    private WebElement staffAssociationListViewRoot;
    private SourceListComponent staffAssociationList;

    @FindBy(css = ".staff-association-users-add-modal .modal-body .apriori-source-list")
    private WebElement candidatesSourceListRoot;

    @FindBy(css = ".staff-association-users-add-modal .modal-footer .btn-primary")
    private WebElement candidatesAddButton;

    @FindBy(css = ".staff-association-users-add-modal .modal-footer .btn-secondary")
    private WebElement candidatesCancelButton;

    @FindBy(css = ".staff-association-users-add-modal .modal-header .close")
    private WebElement candidatesCloseButton;

    @FindBy(css = ".staff-association-user-confirm-modal .modal-footer .btn-primary")
    private WebElement candidatesConfirmOkButton;

    @FindBy(css = ".staff-association-user-confirm-modal .modal-footer .btn-secondary")
    private WebElement candidatesConfirmCancelButton;

    @FindBy(css = ".staff-association-user-confirm-modal .modal-header .close")
    private WebElement candidatesConfirmCloseButton;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver that the page exists on.
     */
    public StaffPage(WebDriver driver) {
        super(driver, log);

        staffAssociationList = new SourceListComponent(getDriver(), staffAssociationListViewRoot);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(addButton);
        getPageUtils().waitForElementToAppear(disableButton);
        getPageUtils().waitForElementToAppear(enableButton);
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
     *
     * @return This object.
     *
     * @throws org.openqa.selenium.ElementNotInteractableException If the button is disabled.
     */
    private StaffPage clickAndWait(final WebElement button) {
        getPageUtils().waitForElementAndClick(button);
        getPageUtils().waitForCondition(staffAssociationList::isStable, PageUtils.DURATION_LOADING);
        return this;
    }

    /**
     * Clicks the enable button.
     *
     * @return This object.
     */
    public StaffPage clickEnableButton() {
        return clickAndWait(enableButton);
    }

    /**
     * Clicks the disable button.
     *
     * @return This object.
     *
     * @throws org.openqa.selenium.ElementNotInteractableException If the button is disabled.
     */
    public StaffPage clickDisableButton() {
        return clickAndWait(disableButton);
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
        WebElement root = getPageUtils().waitForElementToAppear(candidatesSourceListRoot);
        SourceListComponent candidatesSourceList = new SourceListComponent(getDriver(), root);
        getPageUtils().waitForCondition(candidatesSourceList::isStable, PageUtils.DURATION_LOADING);
        return candidatesSourceList;
    }

    public StaffPage clickCandidatesCancelButton() {
        getPageUtils().waitForElementAndClick(candidatesCancelButton);
        return this;
    }

    public StaffPage clickCandidatesCloseButton() {
        getPageUtils().waitForElementAndClick(candidatesCloseButton);
        return this;
    }

    public StaffPage clickCandidatesAddButton() {
        getPageUtils().waitForElementAndClick(candidatesAddButton);
        return this;
    }

    public StaffPage clickCandidatesConfirmCancelButton() {
        getPageUtils().waitForElementAndClick(candidatesConfirmCancelButton);
        return this;
    }

    public StaffPage clickCandidatesConfirmOkButton() {
        getPageUtils().waitForElementAndClick(candidatesConfirmOkButton);
        return this;
    }

    public StaffPage clickCandidatesConfirmCloseButton() {
        getPageUtils().waitForElementAndClick(candidatesConfirmCloseButton);
        return this;
    }
}
