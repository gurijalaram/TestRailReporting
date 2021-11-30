package com.apriori.utils.web.components.modal;

/**
 * Represents a modal that contains a close button in the header.
 */
public interface CloseableModal<C> {
    /**
     * Attempts to click the close button.
     *
     * The close button is different from the secondary button even though they may do the
     * same things.
     *
     * @return The page object model that is returned to after the close button is clicked.
     */
    C clickClose();
}
