package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author cfrith
 */

public class ScenarioTablePage extends LoadableComponent<ScenarioTablePage> {

    private final Logger logger = LoggerFactory.getLogger(ScenarioTablePage.class);

    @FindBy(css = "select[data-ap-field='filter'] option")
    private List<WebElement> workspaceDropdownList;

    @FindBy(css = "select.form-control.input-md.auto-width")
    private WebElement workspaceDropdown;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ScenarioTablePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementsToAppear(workspaceDropdownList);
    }

    /**
     * Selects the workspace from the dropdown
     *
     * @param workspace - workspace dropdown
     * @return current page object
     */
    public ScenarioTablePage selectWorkSpace(String workspace) {
        pageUtils.selectDropdownOption(workspaceDropdown, workspace);
        return this;
    }
}
