package com.apriori.cid.ui.pageobjects.bulkanalysis;

import com.apriori.cid.ui.pageobjects.common.ComponentTableActions;
import com.apriori.cid.ui.pageobjects.common.ScenarioTableController;
import com.apriori.cid.ui.pageobjects.navtoolbars.BulkAnalysisToolbar;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author cfrith
 */

@Slf4j
public class WorksheetsExplorePage extends BulkAnalysisToolbar {

    @FindBy(css = "[id='qa-scenario-explorer-configure-button']")
    private WebElement configureButton;

    @FindBy(css = "[id='qa-scenario-explorer-filter-button'] button")
    private WebElement filterButton;

    @FindBy(css = "[id='qa-sub-component-detail-filter-button'] button")
    private WebElement filterButtonOnTableView;

    @FindBy(css = "[id='qa-scenario-explorer-preview-button'] button")
    private WebElement previewButton;

    @FindBy(id = "qa-scenario-explorer-filter-selector")
    private WebElement filterDropdown;

    @FindBy(css = "[id='qa-scenario-list-filter-selector'] input")
    private WebElement filterInput;

    @FindBy(css = "[id='qa-scenario-explorer-filter-selector']")
    private WebElement currentFilter;

    @FindBy(css = "div.no-content.medium-no-content")
    private WebElement noScenariosMessage;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ScenarioTableController scenarioTableController;
    private ComponentTableActions componentTableActions;

    public WorksheetsExplorePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(setInputsButton);
    }
}
