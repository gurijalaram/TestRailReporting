package com.apriori.shared.util.interfaces;

import java.util.List;

/**
 * Represents a paged list of items.
 *
 * @param <T> The type of data on the page.
 */
public interface Paged<T> {
    /**
     * Gets the total number of items across all pages.
     *
     * @return The total number of items across all pages.
     */
    Integer getTotalItemCount();

    /**
     * Gets the current items on the page.
     *
     * @return The current items on the page.
     */
    List<T> getItems();
}
