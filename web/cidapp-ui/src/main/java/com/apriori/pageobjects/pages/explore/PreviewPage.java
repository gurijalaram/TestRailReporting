package com.apriori.pageobjects.pages.explore;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.StatusIcon;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.utils.enums.StatusIconEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreviewPage extends LoadableComponent<PreviewPage> {

    private static final Logger logger = LoggerFactory.getLogger(PreviewPage.class);

    @FindBy(css = ".scenario-preview-card .card-header")
    private WebElement cardHeader;

    @FindBy(css = ".selection-title")
    private WebElement selectionTitle;

    @FindBy(css = ".scenario-thumbnail.large")
    private WebElement scenarioThumbnailButton;

    @FindBy(css = ".scenario-preview")
    private WebElement previewData;

    @FindBy(css = "[id='qa-scenario-explorer-preview-button'] button")
    private WebElement previewButton;

    @FindBy(css = "[id='qa-sub-component-detail-preview-button']")
    private WebElement previewButtonOnComponentsPage;

    private PageUtils pageUtils;
    private WebDriver driver;
    private StatusIcon statusIcon;
    private ComponentTableActions componentTableActions;

    public PreviewPage(WebDriver driver) {
        this.driver = driver;
        this.statusIcon = new StatusIcon(driver);
        this.pageUtils = new PageUtils(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(cardHeader);
    }

    /**
     * Opens the preview panel
     *
     * @return new page object
     */
    public ExplorePage closePreviewPanel() {
        componentTableActions.closePreviewPanel(previewButton);
        return new ExplorePage(driver);
    }

    /**
     * Gets the selection title/count
     *
     * @return string
     */
    public String getSelectionTitle() {
        return pageUtils.waitForElementToAppear(selectionTitle).getAttribute("textContent");
    }

    /**
     * Checks if the component is present on the page by size == 0 or > 0
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return driver.findElements(getByScenario(componentName, scenarioName)).size();
    }

    /**
     * Removes the scenario from the preview page
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public PreviewPage removeScenario(String componentName, String scenarioName) {
        pageUtils.waitForElementAndClick(driver.findElement(By.xpath(String.format("//div[.='%s / %s']/ancestor::div[@class='list-item']", componentName.toUpperCase().trim(), scenarioName.trim())))
            .findElement(By.cssSelector("[data-icon='times']")));
        return this;
    }

    /**
     * Gets material details - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public double getMaterialResult(String label) {
        By result = By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
        pageUtils.waitForElementToAppear(result);
        return Double.parseDouble(driver.findElement(result).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Gets material details - result is returned as a String with special characters parsed
     *
     * @param label - the label
     * @return String
     */
    public String getMaterialResultText(String label) {
        By result = By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
        pageUtils.waitForElementToAppear(result);
        return driver.findElement(result).getAttribute("textContent").replaceAll("[^0-9?!\\.]", "");
    }

    /**
     * Opens scenario from preview page
     *
     * @return new page object
     */
    public EvaluatePage openScenario() {
        pageUtils.waitForElementAndClick(scenarioThumbnailButton);
        return new EvaluatePage(driver);
    }

    /**
     * Checks image is displayed
     *
     * @return true/false
     */
    public boolean isImageDisplayed() {
        return scenarioThumbnailButton.isDisplayed();
    }

    /**
     * Checks icon is displayed
     *
     * @param icon - the icon
     * @return true/false
     */
    public boolean isIconDisplayed(StatusIconEnum icon) {
        return statusIcon.isIconDisplayed(icon);
    }

    /**
     * Checks preview panel is displayed
     *
     * @return true/false
     */
    public boolean isPreviewPanelDisplayed() {
        return previewData.isDisplayed();
    }

    /**
     * Gets the scenario 'by' locator
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return by
     */
    private By getByScenario(String componentName, String scenarioName) {
        return By.xpath(String.format("//div[.='%s / %s']", componentName.toUpperCase().trim(), scenarioName.trim()));
    }

    /**
     * Opens the preview panel in components tree page
     *
     * @return new page object
     */
    //TODO 12/01/2023 a common page object for the preview page on Explore and ComponentsTable/Tree should be created
    public ComponentsTreePage closePreviewPanelOnComponentsPage() {
        componentTableActions.closePreviewPanel(previewButtonOnComponentsPage);
        return new ComponentsTreePage(driver);
    }
}
