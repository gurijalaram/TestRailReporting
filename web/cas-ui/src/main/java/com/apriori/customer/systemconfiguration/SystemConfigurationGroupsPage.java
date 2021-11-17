package com.apriori.customer.systemconfiguration;

import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.SelectionTreeComponent;

import com.apriori.utils.web.components.SourceListComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.function.Function;

/**
 * Represents the page object model for the system configuration groups.
 */
@Slf4j
public final class SystemConfigurationGroupsPage extends EagerPageComponent<SystemConfigurationGroupsPage> {
    @FindBy(css = ".system-configuration-groups .selection-tree")
    private WebElement selectionTreeRoot;
    private final SelectionTreeComponent groupsTree;

    @FindBy(className = "associated-permissions")
    private WebElement associatedPermissionsRoot;
    private final SourceListComponent associatedPermissions;

    @FindBy(css = ".system-configuration-group-details-card .card-header")
    private WebElement groupDetailsHeader;

    @FindBy(className = "group-details-left")
    private WebElement groupDetailsLeft;

    @FindBy(className = "group-details-right")
    private WebElement groupDetailsRight;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver.
     */
    public SystemConfigurationGroupsPage(WebDriver driver) {
        super(driver, log);
        groupsTree = new SelectionTreeComponent(getDriver(), selectionTreeRoot);
        associatedPermissions = new SourceListComponent(getDriver(), associatedPermissionsRoot);
    }

    /**
     * Gets the groups tree that houses the hierarchy.
     *
     * @return The group tree that houses the hierarchy.
     */
    public SelectionTreeComponent getGroupsTree() {
        return groupsTree;
    }

    /**
     * Gets the associated permissions for the selected group.
     *
     * @return The associated permissions for the selected group.
     */
    public SourceListComponent getAssociatedPermissions() {
        return associatedPermissions;
    }

    /**
     * Gets the label for a value in a given section.
     *
     * @param name The name of the label to search for.
     * @param panel The parent search context element.
     * @return The web element found or null if no such element appears within a reasonable timeframe.
     */
    private WebElement getLabel(String name, WebElement panel) {
        try {
            By query = By.xpath(String.format("//label[.='%s']", name));
            getPageUtils().waitForCondition(() -> panel.findElements(query).size() > 0, PageUtils.DURATION_SLOW);
            return panel.findElement(query);
        } catch (TimeoutException | NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Gets the value element given a name.
     * @param name The name of the field element.
     * @param panel The panel that the value is expected to be in.
     *
     * @return The parsed value or null if the value cannot be parsed.
     */
    private <T> T getValue(String name, WebElement panel, Function<String, T> parse) {
        try {
            By elementQuery = By.className(String.format("read-field-%s", name));
            getPageUtils().waitForCondition(() -> panel.findElements(elementQuery).size() > 0, PageUtils.DURATION_SLOW);
            // It's possible that underneath the value there is a loading indicator.  Wait for that to go away.
            WebElement value = panel.findElement(elementQuery);
            getPageUtils().waitForCondition(() -> getPageUtils().findLoader(value) == null, PageUtils.DURATION_LOADING);
            return parse.apply(value.getText());
        } catch (TimeoutException | NoSuchElementException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Gets the label for number of permissions.
     *
     * @return The label for number of permissions or null if it cannot be found.
     */
    public WebElement getNumberOfPermissionsLabel() {
        return getLabel("Number of Permissions:", groupDetailsLeft);
    }

    /**
     * Gets the number of permissions.
     *
     * @return The number of permissions or null if the value cannot be found.
     */
    public Long getNumberOfPermissions() {
        return getValue("permissionNumber", groupDetailsLeft, Long::parseLong);
    }

    /**
     * Gets the label for the number of subgroups.
     *
     * @return The label for the total number of subgroups or null if it cannot be found.
     */
    public WebElement getNumberOfSubgroupsLabel() {
        return getLabel("Number of Subgroups:", groupDetailsLeft);
    }

    /**
     * Gets the number of subgroups.
     *
     * @return The number of subgroups or null if the value cannot be found.
     */
    public Long getNumberOfSubgroups() {
        return getValue("subgroupNumber", groupDetailsLeft, Long::parseLong);
    }

    /**
     * Gets the label for who created the group.
     *
     * @return The label for who created the group or null if it cannot be found.
     */
    public WebElement getCreatedByLabel() {
        return getLabel("Created By:", groupDetailsLeft);
    }

    /**
     * Gets the user that created the group.
     *
     * @return The user that created the group.
     */
    public String getCreatedBy() {
        return getValue("createdByName", groupDetailsLeft, (v) -> v);
    }

    /**
     * Gets the label for who created the group.
     *
     * @return The label for when the group was created or null if it cannot be found.
     */
    public WebElement getCreatedAtLabel() {
        return getLabel("Created At:", groupDetailsLeft);
    }

    /**
     * Gets the created at value.
     *
     * @return The created at value or null if the value cannot be found.
     */
    public String getCreatedAt() {
        return getValue("createdAt", groupDetailsLeft, (v) -> v);
    }

    /**
     * Gets the label for the group identity.
     *
     * @return The label for the group identity or null if it cannot be found.
     */
    public WebElement getIdentityLabel() {
        return getLabel("Identity:", groupDetailsRight);
    }

    /**
     * Gets the identity of the group.
     *
     * @return The identity of the group or null if it cannot be found.
     */
    public String getIdentity() {
        return getValue("identity", groupDetailsRight, (v) -> v);
    }

    /**
     * Gets the label for the last person to update the group.
     *
     * @return The label for the last person to update the group.
     */
    public WebElement getUpdatedByLabel() {
        return getLabel("Updated By:", groupDetailsRight);
    }

    /**
     * Gets the user who last updated the group.
     *
     * @return The user that last updated the group or null if it cannot be found.  This will
     *         be the empty string if the value exists but nobody ever updated the group.
     */
    public String getUpdatedBy() {
        return getValue("updatedByName", groupDetailsRight, (v) -> v);
    }

    /**
     * Gets the label for the last time the group was updated.
     *
     * @return The label for the last time the group was updated.
     */
    public WebElement getUpdatedAtLabel() {
        return getLabel("Updated At:", groupDetailsRight);
    }

    /**
     * Gets the last time the group was updated.
     *
     * @return The last time the group was updated or null if the value cannot be found.  This will
     *         return the empty string if the value exists but nobody ever updated the group.
     */
    public String getUpdatedAt() {
        return getValue("updatedAt", groupDetailsRight, (v) -> v);
    }

    /**
     * Gets the label for the description.
     *
     * @return The label for the description.
     */
    public WebElement getDescriptionLabel() {
        return getLabel("Description:", groupDetailsRight);
    }

    /**
     * Gets the description of the group.
     *
     * @return The description value or null if it cannot be found.
     */
    public String getDescription() {
        return getValue("description", groupDetailsRight, (v) -> v);
    }

    /**
     * Gets the header element of the details panel.
     *
     * @return The header text of the details panel.
     */
    public String getDetailsHeader() {
        return groupDetailsHeader.getText();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(selectionTreeRoot);
        getPageUtils().waitForElementToAppear(groupDetailsHeader);
        getPageUtils().waitForElementToAppear(groupDetailsLeft);
        getPageUtils().waitForElementToAppear(groupDetailsRight);
    }
}
