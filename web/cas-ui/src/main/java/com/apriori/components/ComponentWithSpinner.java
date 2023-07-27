package com.apriori.components;

/**
 * Represents a component that can be in a loading state.
 */
public interface ComponentWithSpinner {

    /**
     * Gets whether this component is stable and not spinning.
     *
     * @return True if the component is stable.  False if spinning.
     */
    boolean isStable();
}
