package com.apriori.pageobjects.pages.evaluate.components;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import com.utils.ButtonTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ComponentsTreePage extends LoadableComponent<ComponentsTreePage> {

    @FindBy(css = "div[data-testid='loader']")
    private WebElement loadingSpinner;

    @FindBy(css = "[id='qa-scenario-list-card-view-button'] button")
    private WebElement treeViewButton;

    @FindBy(css = "[id='qa-scenario-list-table-view-button'] button")
    private WebElement tableViewButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-exclude-button'] button")
    private WebElement excludeButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-include-button'] button")
    private WebElement includeButton;

    @FindBy(css = ".component-display-name-container [data-icon='arrow-up-right-from-square']")
    private WebElement subcomponentCard;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public ComponentsTreePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Tree View is not active", pageUtils.waitForElementToAppear(treeViewButton).getAttribute("class").contains("active"));
        pageUtils.waitForElementNotVisible(loadingSpinner, 1);
    }

    /**
     * Open table view
     *
     * @return new page object
     */
    public ComponentsTablePage selectTableView() {
        pageUtils.waitForElementAndClick(tableViewButton);
        return new ComponentsTablePage(driver);
    }

    /**
     * Selects the include or the exclude button
     *
     * @return - the current page object
     */
    public ComponentsTreePage selectButtonType(ButtonTypeEnum buttonTypeEnum) {
        WebElement buttonToPress;

        switch (buttonTypeEnum) {
            case INCLUDE:
                buttonToPress = includeButton;
                break;
            case EXCLUDE:
                buttonToPress = excludeButton;
                break;
            default:
                return this;
        }
        pageUtils.waitForElementAndClick(buttonToPress);
        pageUtils.waitForElementNotDisplayed(subcomponentCard, 1);
        return this;
    }

    /**
     * gets the sub component in a sub assembly
     *
     * @param componentName - the component name
     * @return - current page object
     */
    public ComponentsTreePage selectSubAssemblySubComponent(String componentName, String subAssemblyName) {
        By scenario = with(getXpath(componentName))
            .below(By.xpath(String.format("//span[text()='%s']", subAssemblyName.toUpperCase().trim())));
        pageUtils.waitForElementAndClick(scenario);
        return this;
    }

    private By getXpath(String componentName) {
        return By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//span[@data-testid='checkbox']", componentName.toUpperCase().trim()));
    }

    /**
     * Gets the struck out component name
     *
     * @param componentName - the component name
     * @return - string
     */
    public boolean isTextDecorationStruckOut(String componentName) {
        By byComponentName = By.xpath(String.format("//ancestor::div[@role='row']//span[contains(text(),'%s')]/ancestor::div[@role='row']",
            componentName.toUpperCase().trim()));
        return pageUtils.waitForElementToAppear(byComponentName).getCssValue("text-decoration").contains("line-through");
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
