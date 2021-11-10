package com.apriori.utils;

import java.util.Comparator;
import java.util.List;

public class ListUtils {
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
