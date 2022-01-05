package com.apriori.customer.users;

import com.apriori.common.ModalUserList;
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

    private ModalUserList modalUserList;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver that the page exists on.
     */
    public StaffPage(WebDriver driver) {
        super(driver, log);
        staffAssociationList = new SourceListComponent(getDriver(), staffAssociationListViewRoot);
        modalUserList = new ModalUserList(driver);
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
}
