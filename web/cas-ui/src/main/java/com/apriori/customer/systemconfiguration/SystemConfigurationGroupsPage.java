package com.apriori.customer.systemconfiguration;

import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.SelectionTreeComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the page object model for the system configuration groups.
 */
@Slf4j
public final class SystemConfigurationGroupsPage extends EagerPageComponent<SystemConfigurationGroupsPage> {
    @FindBy(css = ".system-configuration-groups .selection-tree")
    private WebElement selectionTreeRoot;
    private final SelectionTreeComponent groupsTree;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver.
     */
    public SystemConfigurationGroupsPage(WebDriver driver) {
        super(driver, log);
        groupsTree = new SelectionTreeComponent(getDriver(), selectionTreeRoot);
    }

    public SelectionTreeComponent getGroupsTree() {
        return groupsTree;
    }

    /**
     * Checks to make sure that the selection tree is available.
     *
     * @throws Error If the selection tree cannot be found.
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(selectionTreeRoot);
    }
}
