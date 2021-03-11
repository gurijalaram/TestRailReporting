package com.apriori.pageobjects.pages.evaluate.components;

import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentsListPage extends LoadableComponent<ComponentsListPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(ComponentsListPage.class);

    @FindBy(css = ".evaluate-view-drawer [data-icon='list']")
    private WebElement listButton;

    @FindBy(css = ".evaluate-view-drawer [data-icon='folder-tree']")
    private WebElement treeButton;

    @FindBy(xpath = "//button[.='Preview']")
    private WebElement previewButton;

    @FindBy(xpath = "//button[.='Selection']")
    private WebElement selectionButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;
    private ComponentTableActions componentTableActions;
    private ScenarioTableController scenarioTableController;

    public ComponentsListPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAndClick(listButton);
        pageUtils.waitForElementAndClick(previewButton);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController enterSearch(String componentName) {
        return componentTableActions.enterSearch(componentName);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController clickSearch(String componentName) {
        return componentTableActions.clickSearch(componentName);
    }

    /**
     * Open configure page
     *
     * @return new page object
     */
    public ConfigurePage configure() {
        return componentTableActions.configure();
    }

    /**
     * Open filters page
     *
     * @return new page object
     */
    public FilterPage filter() {
        return componentTableActions.filter();
    }


    /**
     * Opens tree view tab
     *
     * @return new page object
     */
    public TreePage openTreeTab() {
        pageUtils.waitForElementAndClick(treeButton);
        return new TreePage(driver);
    }

    /**
     * Opens the help page
     *
     * @return new page object
     */
    public HelpDocPage openHelp() {
        return panelController.openHelp();
    }

    /**
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }

}