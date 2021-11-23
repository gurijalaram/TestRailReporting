package com.apriori.customer.systemconfiguration;

import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.SelectionTreeComponent;

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
 * Represents the page object model of the system configuration permissions page.
 */
@Slf4j
public final class SystemConfigurationPermissionsPage extends EagerPageComponent<SystemConfigurationPermissionsPage> {
    @FindBy(css = ".system-configuration-permissions .selection-tree")
    private WebElement permissionsListRoot;

    @FindBy(css = ".system-configuration-permissions .right-details .card-header")
    private WebElement permissionDetailsHeader;

    @FindBy(className = "left-tree-title-count")
    private WebElement permissionNumberLabel;

    @FindBy(css = ".edit-read-form .card-body")
    private WebElement permissionContent;

    @FindBy(name = "cslRule")
    private WebElement cslRule;

    @FindBy(name = "expression")
    private WebElement javaScriptRule;

    private final SelectionTreeComponent permissionsList;

    private PageUtils pageUtils;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver.
     */
    public SystemConfigurationPermissionsPage(WebDriver driver) {
        super(driver, log);
        permissionsList = new SelectionTreeComponent(getDriver(), permissionsListRoot);
        pageUtils = getPageUtils();
    }

    /**
     * Gets the permissions list.
     *
     * @return The permissions list.
     */
    public SelectionTreeComponent getPermissionsList() {
        return permissionsList;
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
            getPageUtils().waitForCondition(() -> panel.findElements(elementQuery).size() > 0, Duration.ofMillis(500));
            // It's possible that underneath the value there is a loading indicator.  Wait for that to go away.
            WebElement value = panel.findElement(elementQuery);
            By loaderQuery = By.className("read-field-loading");
            getPageUtils().waitForCondition(() -> value.findElements(loaderQuery).size() == 0, Duration.ofSeconds(10));
            return parse.apply(value.getText());
        } catch (TimeoutException | NoSuchElementException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Gets the label for the description.
     *
     * @return The label for the description.
     */
    public WebElement getDescriptionLabel() {
        return pageUtils.findElementByText("label", "Description: ", permissionContent);
    }

    /**
     * Gets the description of the group.
     *
     * @return The description value or null if it cannot be found.
     */
    public String getDescription() {
        return getValue("description", permissionContent, (v) -> v);
    }

    /**
     * Gets the label for the Resource.
     *
     * @return The label for the Resource.
     */
    public WebElement getResourceLabel() {
        return pageUtils.findElementByText("label", "Resource: ", permissionContent);
    }

    /**
     * Gets the Resource of the group.
     *
     * @return The Resource value or null if it cannot be found.
     */
    public String getResource() {
        return getValue("resource", permissionContent, (v) -> v);
    }

    /**
     * Gets the label for the Action.
     *
     * @return The label for the Action.
     */
    public WebElement getActionLabel() {
        return pageUtils.findElementByText("label", "Action: ", permissionContent);
    }

    /**
     * Gets the actions of the group.
     *
     * @return The actions value or null if it cannot be found.
     */
    public String getActions() {
        return getValue("actions", permissionContent, (v) -> v);
    }

    /**
     * Gets the label for the Grant.
     *
     * @return The label for the Grant.
     */
    public WebElement getGrantLabel() {
        return pageUtils.findElementByText("label", "Grant: ", permissionContent);
    }

    /**
     * Gets the Grant of the group.
     *
     * @return The Grant value or null if it cannot be found.
     */
    public String getGrant() {
        return getValue("grant", permissionContent, (v) -> v);
    }

    /**
     * Gets the label for the Deny.
     *
     * @return The label for the Deny.
     */
    public WebElement getDenyLabel() {
        return pageUtils.findElementByText("label", "Deny: ", permissionContent);
    }

    /**
     * Gets the Deny of the group.
     *
     * @return The Deny value or null if it cannot be found.
     */
    public String getDeny() {
        return getValue("deny", permissionContent, (v) -> v);
    }

    /**
     * Gets the label for the CSL Rule.
     *
     * @return The label for the CSL Rule.
     */
    public WebElement getCSLRuleLabel() {
        return pageUtils.findElementByText("label", "CSL Rule: ", permissionContent);
    }

    /**
     * Gets the CSL Rule of the group.
     *
     * @return The CSL Rule value or null if it cannot be found.
     */
    public WebElement getCSLRule() {
        return cslRule;
    }

    /**
     * Gets the label for the JS Rule.
     *
     * @return The label for the JS Rule.
     */
    public WebElement getJSRuleLabel() {
        return pageUtils.findElementByText("label", "JavaScript:", permissionContent);
    }

    /**
     * Gets the JS Rule of the group.
     *
     * @return The JS Rule value or null if it cannot be found.
     */
    public WebElement getJSRule() {
        return javaScriptRule;
    }

    /**
     * Gets the count of permissions.
     *
     * @return The permissions count.
     */
    public Integer getPermissionLabelCount() {
        String bracketedCount = permissionNumberLabel.getText();
        String count = bracketedCount.substring(bracketedCount.indexOf("(") + 1, bracketedCount.indexOf(")"));
        return Integer.parseInt(count);
    }

    /**
     * Gets the header element of the details panel.
     *
     * @return The header of the details panel.
     */
    public WebElement getDetailsHeader() {
        return permissionDetailsHeader;
    }

    /**
     * Checks to make sure that the selection tree is available.
     *
     * @throws Error If the selection tree cannot be found.
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(permissionsListRoot);
    }
}
