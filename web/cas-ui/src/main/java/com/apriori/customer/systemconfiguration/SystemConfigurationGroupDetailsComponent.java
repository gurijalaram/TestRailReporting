package com.apriori.customer.systemconfiguration;

import com.apriori.utils.web.components.CommonComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.function.Function;

/**
 * Represents the group details that renders a selected group in the system configuration groups page.
 *
 * This component is always displayed, but it's content changes based on the context of the group that
 * is selected.  Therefore, this component is not immutable and an invocation to one of the underlying methods
 * can yield different results without doing another query of the dom for the root card.
 *
 * This component is also stricter as to where things are located, often using a parent dom element to search
 * for the necessary children.
 */
@Slf4j
public class SystemConfigurationGroupDetailsComponent extends CommonComponent {
    /**
     * Initializes a new instance of this object.
     *
     * @param driver The overall global web driver that is querying different pages.
     * @param root   The root element to attach for this component.
     */
    public SystemConfigurationGroupDetailsComponent(WebDriver driver, WebElement root) {
        super(driver, root);
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
            getPageUtils().waitForCondition(() -> panel.findElements(query).size() > 0, Duration.ofMillis(500));
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
     * @return The web element found or null if no such element appears withing a reasonable timeframe.  If the
     *         value is a loadable value, then the maximum amount of time given for a load will be 10 seconds.
     */
    private WebElement getValue(String name, WebElement panel) {
        try {
            By elementQuery = By.className(String.format("read-field-%s", name));
            getPageUtils().waitForCondition(() -> panel.findElements(elementQuery).size() > 0, Duration.ofMillis(500));
            // It's possible that underneath the value there is a loading indicator.  Wait for that to go away.
            WebElement value = panel.findElement(elementQuery);
            By loaderQuery = By.className("read-field-loading");
            getPageUtils().waitForCondition(() -> value.findElements(loaderQuery).size() == 0, Duration.ofSeconds(10));
            return value;
        } catch (TimeoutException | NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Gets the value of a named field.
     *
     * @param name The field name to get the value for.
     * @param panel The panel that the value exists in.
     * @param parse A parse method that transforms the value text.
     * @param <T> The type of data to parse and return.  If the parse fails
     *
     * @return The parsed value or null if the value cannot be parsed.
     */
    private <T> T getValue(String name, WebElement panel, Function<String, T> parse) {
        WebElement element = getValue(name, panel);

        if (element == null) {
            return null;
        }

        try {
            return parse.apply(element.getText());
        } catch (IllegalArgumentException e) {
            log.debug(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets the left or right details panel.
     *
     * @param location "left" or "right"
     *
     * @return The web element that corresponds to the given details panel.
     */
    private WebElement getDetailsPanel(String location) {
        By query = By.className(String.format("group-details-%s", location));
        getPageUtils().waitForCondition(() -> getRoot().findElements(query).size() > 0, Duration.ofMillis(500));
        getPageUtils().waitForCondition(() -> getRoot().findElement(query).isDisplayed(), Duration.ofMillis(500));
        return getRoot().findElement(query);
    }

    /**
     * Gets the details panel on the left.
     *
     * @return The details panel on the left.
     */
    private WebElement getLeftDetailsPanel() {
        return getDetailsPanel("left");
    }

    /**
     * Gets the details panel on the right.
     *
     * @return The details panel on the right.
     */
    private WebElement getRightDetailsPanel() {
        return getDetailsPanel("right");
    }

    /**
     * Gets the label for number of permissions.
     *
     * @return The label for number of permissions or null if it cannot be found.
     */
    public WebElement getNumberOfPermissionsLabel() {
        return getLabel("Number of Permissions:", getLeftDetailsPanel());
    }

    /**
     * Gets the number of permissions.
     *
     * @return The number of permissions or null if the value cannot be found.
     */
    public Long getNumberOfPermissions() {
        return getValue("permissionNumber", getLeftDetailsPanel(), Long::parseLong);
    }

    /**
     * Gets the label for the number of subgroups.
     *
     * @return The label for the total number of subgroups or null if it cannot be found.
     */
    public WebElement getNumberOfSubgroupsLabel() {
        return getLabel("Number of Subgroups:", getLeftDetailsPanel());
    }

    /**
     * Gets the number of subgroups.
     *
     * @return The number of subgroups or null if the value cannot be found.
     */
    public Long getNumberOfSubgroups() {
        return getValue("subgroupNumber", getLeftDetailsPanel(), Long::parseLong);
    }

    /**
     * Gets the label for who created the group.
     *
     * @return The label for who created the group or null if it cannot be found.
     */
    public WebElement getCreatedByLabel() {
        return getLabel("Created By:", getLeftDetailsPanel());
    }

    /**
     * Gets the user that created the group.
     *
     * @return The user that created the group.
     */
    public String getCreatedBy() {
        return getValue("createdByName", getLeftDetailsPanel(), (v) -> v);
    }

    /**
     * Gets the label for who created the group.
     *
     * @return The label for when the group was created or null if it cannot be found.
     */
    public WebElement getCreatedAtLabel() {
        return getLabel("Created At:", getLeftDetailsPanel());
    }

    /**
     * Gets the created at value.
     *
     * @return The created at value or null if the value cannot be found.
     */
    public String getCreatedAt() {
        return getValue("createdAt", getLeftDetailsPanel(), (v) -> v);
    }

    /**
     * Gets the label for the group identity.
     *
     * @return The label for the group identity or null if it cannot be found.
     */
    public WebElement getIdentityLabel() {
        return getLabel("Identity:", getRightDetailsPanel());
    }

    /**
     * Gets the identity of the group.
     *
     * @return The identity of the group or null if it cannot be found.
     */
    public String getIdentity() {
        return getValue("identity", getRightDetailsPanel(), (v) -> v);
    }

    /**
     * Gets the label for the last person to update the group.
     *
     * @return The label for the last person to update the group.
     */
    public WebElement getUpdatedByLabel() {
        return getLabel("Updated By:", getRightDetailsPanel());
    }

    /**
     * Gets the user who last updated the group.
     *
     * @return The user that last updated the group or null if it cannot be found.  This will
     *         be the empty string if the value exists but nobody ever updated the group.
     */
    public String getUpdatedBy() {
        return getValue("updatedByName", getRightDetailsPanel(), (v) -> v);
    }

    /**
     * Gets the label for the last time the group was updated.
     *
     * @return The label for the last time the group was updated.
     */
    public WebElement getUpdatedAtLabel() {
        return getLabel("Updated At:", getRightDetailsPanel());
    }

    /**
     * Gets the last time the group was updated.
     *
     * @return The last time the group was updated or null if the value cannot be found.  This will
     *         return the empty string if the value exists but nobody ever updated the group.
     */
    public String getUpdatedAt() {
        return getValue("updatedAt", getRightDetailsPanel(), (v) -> v);
    }

    /**
     * Gets the label for the description.
     *
     * @return The label for the description.
     */
    public WebElement getDescriptionLabel() {
        return getLabel("Description:", getRightDetailsPanel());
    }

    /**
     * Gets the description of the group.
     *
     * @return The description value or null if it cannot be found.
     */
    public String getDescription() {
        return getValue("description", getRightDetailsPanel(), (v) -> v);
    }

    /**
     * Gets the header element of the details panel.
     *
     * @return The header text of the details panel.
     */
    public String getHeader() {
        return getRoot().findElement(By.className("card-header")).getText();
    }
}
