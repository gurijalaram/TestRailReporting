package com.apriori.cas.ui.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an item in a selection tree.
 */
public class SelectionTreeItemComponent extends CommonComponent {
    /**
     * Initializes a new instance of this object.
     *
     * @param driver The overall global web driver that is querying different pages.
     * @param root   The root element to attach for this component.
     * @throws Error Occurs if there is a problem loading child components.
     */
    public SelectionTreeItemComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    /**
     * Gets the content element root for this selection tree item.
     *
     * This is required and should always be available.
     *
     * @return The DOM element where the content lives.
     */
    private WebElement getContent() {
        return this.getRoot().findElement(By.className("selection-tree-node-content"));
    }

    /**
     * Gets the expander element for this selection tree item.
     *
     * This is a required element but will be invisible for items that do not have any children.
     *
     * @return The DOM element where the expander lives.
     */
    private WebElement getExpander() {
        return this.getRoot().findElement(By.className("selection-tree-expander"));
    }

    /**
     * Gets the optional sub-tree for this item.
     *
     * @return The sub-tree.  If this item is collapsed, or is not expandable, then this method
     *         returns null.
     */
    private WebElement getSubTree() {
        return this.getRoot()
            .findElements(By.className("selection-subtree"))
            .stream()
            .findFirst()
            .orElse(null);
    }

    /**
     * Gets a value that indicates if this item is selected.
     *
     * @return True if this item is selected.  False otherwise.
     */
    public boolean isSelected() {
        return getPageUtils().doesElementHaveClass(getContent(), "selected");
    }

    /**
     * Gets the text of this item.
     *
     * @return The text of this item.
     */
    public String getText() {
        return getContent().getText();
    }

    /**
     * Gets a value that determines if this item can be expanded or collapsed.
     *
     * @return True if an expander is displayed for this item, false otherwise.
     */
    public boolean isExpandable() {
        return getExpander().isDisplayed();
    }

    /**
     * Gets a value that determines if this selection tree item is expanded.
     *
     * @return True if the expander is available and the subtree is displayed.
     *         False otherwise.
     */
    public boolean isExpanded() {
        return isExpandable() && getSubTree() != null;
    }

    /**
     * Clicks the expander if this item is expandable.
     *
     * @return This object
     */
    public SelectionTreeItemComponent toggle() {
        if (!isExpandable()) {
            return this;
        }

        getPageUtils().scrollWithJavaScript(getRoot(), true);
        getPageUtils().waitForElementAndClick(getExpander());
        return this;
    }

    /**
     * Clicks the expander if this item is in the collapsed state.
     *
     * Otherwise, this does nothing.
     *
     * @return This object
     */
    public SelectionTreeItemComponent expand() {
        if (isExpanded()) {
            return this;
        }
        return toggle();

    }

    /**
     * Clicks the expander if this item is in the expanded state.
     *
     * Otherwise, this does nothing.
     *
     * @return This object.
     */
    public SelectionTreeItemComponent collapse() {
        if (!isExpanded()) {
            return this;
        }
        return toggle();
    }

    /**
     * Clicks on the root item to select it.
     *
     * @return This object
     */
    public SelectionTreeItemComponent select() {
        getPageUtils().scrollWithJavaScript(this.getRoot(), true);
        getPageUtils().waitForElementAndClick(this.getRoot());
        return this;
    }

    /**
     * Gets the collection of child items for this item.
     *
     * @return The items underneath this item.
     */
    public List<SelectionTreeItemComponent> getChildren() {
        if (!isExpanded()) {
            return Collections.emptyList();
        }

        List<WebElement> children = this.getRoot().findElements(By.xpath("*"));

        return children.stream()
            .filter((ch) -> getPageUtils().doesElementHaveClass(ch, "selection-tree-node"))
            .map((ch) -> new SelectionTreeItemComponent(getDriver(), ch))
            .collect(Collectors.toList());
    }
}
