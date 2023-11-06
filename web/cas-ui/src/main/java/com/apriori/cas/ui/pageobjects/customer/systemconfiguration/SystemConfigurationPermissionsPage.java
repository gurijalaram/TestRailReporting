package com.apriori.cas.ui.pageobjects.customer.systemconfiguration;

import com.apriori.cas.ui.components.SelectionTreeComponent;
import com.apriori.web.app.util.EagerPageComponent;
import com.apriori.web.app.util.PageUtils;

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
     * Gets the value for the given name.
     *
     * @return The value for the given name.
     */
    public String getValue(String name) {
        return getValue(name, permissionContent, (v) -> v);
    }

    /**
     * Gets the label for the given name.
     *
     * @return The label for the given name.
     */
    public WebElement getLabel(String name) {
        return pageUtils.findElementByText("label", name, permissionContent);
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
