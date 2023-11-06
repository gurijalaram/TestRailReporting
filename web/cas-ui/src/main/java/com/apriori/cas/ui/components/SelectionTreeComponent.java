package com.apriori.cas.ui.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a selection tree which allows for collapsable and expandable nodes.
 */
public final class SelectionTreeComponent extends CommonComponent {

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The overall global web driver that is querying different pages.
     * @param root   The root element to attach for this component.
     * @throws Error Occurs if there is a problem loading child components.
     */
    public SelectionTreeComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    /**
     * Gets the immediate children of the tree.
     *
     * This is all the root nodes.
     *
     * @return The collection of all root nodes for this tree.
     */
    public List<SelectionTreeItemComponent> getHierarchy() {
        List<WebElement> elements = this.getRoot().findElements(By.xpath("*"));
        return elements.stream()
            .filter((e) -> getPageUtils().doesElementHaveClass(e, "selection-tree-node"))
            .map((e) -> new SelectionTreeItemComponent(getDriver(), e))
            .collect(Collectors.toList());
    }

    /**
     * Gets every item in the tree flattened in the order they are displayed.
     *
     * @return The hierarchy flattened into a single list.
     */
    public List<SelectionTreeItemComponent> getFlatHierarchy() {
        List<WebElement> elements = this.getRoot().findElements(By.className("selection-tree-node"));
        return elements.stream()
            .map((e) -> new SelectionTreeItemComponent(getDriver(), e))
            .collect(Collectors.toList());
    }
}
