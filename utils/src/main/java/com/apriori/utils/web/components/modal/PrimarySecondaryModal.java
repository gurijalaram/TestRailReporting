package com.apriori.utils.web.components.modal;

/**
 * Represents a modal that flows from a primary and a secondary element.
 *
 * @param <S> The object modal that is returned to when the secondary element is clicked.
 * @param <P> The object modal that is returned to when the primary element is clicked.
 */
public interface PrimarySecondaryModal<S, P> {
    /**
     * Clicks the secondary element and returns the object model flow.
     *
     * @return The page object model that should be moved to when the secondary element is clicked.
     */
    S clickSecondary();

    /**
     * Clicks the primary element and returns the object model flow.
     *
     * @return The page object model that should be moved to when the primary element is clicked.
     */
    P clickPrimary();
}
