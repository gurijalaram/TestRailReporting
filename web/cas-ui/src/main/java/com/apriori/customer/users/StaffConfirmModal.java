package com.apriori.customer.users;

import com.apriori.utils.web.components.modal.CloseableModal;
import com.apriori.utils.web.components.modal.ModalComponent;
import com.apriori.utils.web.components.modal.PrimarySecondaryModal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Represents the message box modal that confirms the
 */
public final class StaffConfirmModal extends ModalComponent<StaffConfirmModal> implements PrimarySecondaryModal<StaffAddModal, StaffPage>, CloseableModal<StaffAddModal> {
    private static final By BY_CANCEL_BUTTON = By.className("btn-secondary");
    private static final By BY_OK_BUTTON = By.className("btn-primary");

    /**
     * @inheritDoc
     */
    public StaffConfirmModal(WebDriver driver) {
        super(driver, "staff-association-user-confirm-modal");
    }

    /**
     * Clicks the Cancel button.
     *
     * @return Back to the staff add modal component.
     */
    @Override
    public StaffAddModal clickSecondary() {
        clickFooterElement(BY_CANCEL_BUTTON);
        return new StaffAddModal(getDriver());
    }

    /**
     * Clicks the primary button.
     *
     * @return Back to the staff page.
     */
    @Override
    public StaffPage clickPrimary() {
        clickFooterElement(BY_OK_BUTTON);
        return new StaffPage(getDriver());
    }

    /**
     * @inheritDoc
     */
    @Override
    public StaffAddModal clickClose() {
        clickCloseButtonInHeader();
        return new StaffAddModal(getDriver());
    }
}
