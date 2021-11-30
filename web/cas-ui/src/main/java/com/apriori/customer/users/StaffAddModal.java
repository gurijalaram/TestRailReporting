package com.apriori.customer.users;

import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.modal.CloseableModal;
import com.apriori.utils.web.components.modal.ModalComponent;
import com.apriori.utils.web.components.modal.PrimarySecondaryModal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents the modal for adding a user.
 */
public final class StaffAddModal extends ModalComponent<StaffAddModal> implements PrimarySecondaryModal<StaffPage, StaffConfirmModal>, CloseableModal<StaffPage> {
    private static final By BY_CANCEL_BUTTON = By.className("btn-secondary");
    private static final By BY_ADD_BUTTON = By.className("btn-primary");
    private static final By BY_SOURCE_LIST = By.className("apriori-source-list");

    /**
     * @inheritDoc
     */
    public StaffAddModal(WebDriver driver) {
        super(driver, "staff-association-users-add-modal");
    }

    /**
     * Gets the underlying users source list.
     *
     * @return The users source list.
     */
    public SourceListComponent getUsers() {
        WebElement root = getPageUtils().waitForElementToAppear(BY_SOURCE_LIST, PageUtils.DURATION_FAST, getBodyRoot());
        SourceListComponent users = new SourceListComponent(getDriver(), root);
        getPageUtils().waitForCondition(users::isStable, PageUtils.DURATION_LOADING);
        return users;
    }

    /**
     * @inheritDoc
     */
    @Override
    public StaffPage clickSecondary() {
        clickFooterElement(BY_CANCEL_BUTTON);
        return new StaffPage(getDriver());
    }

    /**
     * @inheritDoc
     */
    @Override
    public StaffConfirmModal clickPrimary() {
        clickFooterElement(BY_ADD_BUTTON);
        return new StaffConfirmModal(getDriver());
    }

    /**
     * @inheritDoc
     */
    @Override
    public StaffPage clickClose() {
        clickCloseButtonInHeader();
        return new StaffPage(getDriver());
    }
}
