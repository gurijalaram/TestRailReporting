package com.apriori.cas.ui.utils;

import java.util.Comparator;
import java.util.List;

/**
 * Represents a list of utilities that manipulate java.util.List based objects.
 */
public abstract class ListUtils {
    /**
     * Gets a value that determines if items in a list are sorted.
     * <p>
     * The empty list and a list containing only one item is considered sorted.
     *
     * @param items   The items to check.
     * @param compare The compare operator that determines if x is less than y.
     * @param <T>     The type of each item
     * @return True if it is the case that for each index x, compare(items[x], items[x+1]) <= 0. False
     * otherwise.
     */
    public static <T> boolean isSorted(List<T> items, Comparator<T> compare) {
        for (int current = 1; current < items.size(); ++current) {
            int previous = current - 1;
            T x = items.get(previous);
            T y = items.get(current);
            int comparison = compare.compare(x, y);

            if (comparison > 0) {
                return false;
            }
        }

        return true;
    }
}