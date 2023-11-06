package com.apriori.cas.ui.components;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents a flexible component that changes the route navigation when clicked.
 *
 * This can be a tab, link, button, or any other input which will change the
 * url route.
 */
public final class RoutingComponent extends CommonComponent {
    /**
     * Initializes a new instance of this object.
     *
     * @param driver The overall global web driver that is querying different pages.
     * @param root   The root element to attach for this component.
     */
    public RoutingComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    /**
     * Gets a value that determines if this component is marked active.
     *
     * @return True if the root element has an active class.
     */
    public boolean isActive() {
        return getPageUtils().doesElementHaveClass(getRoot(), "active");
    }

    /**
     * Gets a value that determines if this component is enabled.
     *
     * @return False if the root element has a disabled attribute or a disabled class.  True otherwise.
     */
    public boolean isEnabled() {
        return !StringUtils.equalsIgnoreCase("true", getRoot().getAttribute("disabled")) &&
            !getPageUtils().doesElementHaveClass(getRoot(), "disabled");
    }

    /**
     * Invokes the navigation.
     *
     * If this route is already active, then this does nothing.
     */
    public void navigate() {
        if (!isActive()) {
            getPageUtils().waitForElementAndClick(getRoot());
        }
    }
}
