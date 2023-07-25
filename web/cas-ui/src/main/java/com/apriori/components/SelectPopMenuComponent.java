package com.apriori.components;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Represents the popup menu that is produced by a SelectComponent.
 */
public final class SelectPopMenuComponent extends CommonComponent {
    private List<WebElement> items;

    /**
     * Initializes a new instance of this object.
     *
     * @param parent The parent select component that will show this popup menu.
     */
    public SelectPopMenuComponent(SelectComponent parent) {
        super(parent.getDriver(), parent.getRoot());

        items = getRoot().findElements(By.className("select-menu-item"));
    }

    /**
     * Clicks on one of the child element items based on the inner text.
     *
     * @param item The text of the item to select.  Note that this is CASE SENSITIVE.
     */
    public void select(String item) {
        items.stream()
            .filter((element) -> StringUtils.equals(element.getText(), item))
            .findFirst()
            .ifPresent(WebElement::click);
    }
}
