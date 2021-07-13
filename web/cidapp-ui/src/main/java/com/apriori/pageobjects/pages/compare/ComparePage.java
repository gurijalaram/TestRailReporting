package com.apriori.pageobjects.pages.compare;

import com.apriori.pageobjects.common.StatusIcon;
import com.apriori.pageobjects.navtoolbars.CompareToolbar;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.StatusIconEnum;

import com.utils.ComparisonCardEnum;
import com.utils.ComparisonDeltaEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

public class ComparePage extends CompareToolbar {

    private static final Logger logger = LoggerFactory.getLogger(ComparePage.class);

    @FindBy(css = "[data-rbd-droppable-id='header-sections']")
    private WebElement headerSections;

    @FindBy(css = "[placeholder='Description']")
    private WebElement descriptionInput;

    @FindBy(css = ".basis-column .apriori-card .card-header")
    private WebElement basisColumnHeader;

    @FindBy(css = "[data-rbd-droppable-id='basis-column']")
    private WebElement basisColumn;

    @FindBy(css = "[data-rbd-droppable-id='basis-column'] .close-button")
    private WebElement deleteBasis;

    @FindBy(xpath = "//p[.='Preparing Comparison']")
    private List<WebElement> comparisonLoader;

    private PageUtils pageUtils;
    private WebDriver driver;
    private StatusIcon statusIcon;

    public ComparePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.statusIcon = new StatusIcon(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementAppear(headerSections);
        pageUtils.invisibilityOfElements(comparisonLoader);
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
     * Inputs the description
     *
     * @param description - the description
     * @return current page object
     */
    public ComparePage inputDescription(String description) {
        descriptionInput.clear();
        descriptionInput.sendKeys(description);
        return this;
    }

    /**
     * Expands section
     *
     * @param section - the section
     * @return current page object
     */
    public ComparePage expand(String section) {
        expandCollapseSection(section, "arrow-down");
        return this;
    }

    /**
     * Collapses section
     *
     * @param section - the section
     * @return current page object
     */
    public ComparePage collapse(String section) {
        expandCollapseSection(section, "arrow-up");
        return this;
    }

    /**
     * Performs expand/collapse on section
     *
     * @param section - the section
     * @param action  - the action
     */
    private void expandCollapseSection(String section, String action) {
        By byChevron = By.cssSelector(String.format("[data-rbd-drag-handle-draggable-id='%s'] [data-icon='chevron-down']", section));
        WebElement chevron = driver.findElement(byChevron);
        if (chevron.getAttribute("class").contains(action)) {
            pageUtils.waitForElementAndClick(chevron);
        }
    }

    /**
     * Gets the card of each section
     *
     * @param section - the section
     * @return list of string
     */
    public List<String> getCard(String section) {
        List<WebElement> cards = driver.findElements(By.cssSelector(String.format("[data-rbd-drag-handle-draggable-id='%s'] .comparison-row.comparison-summary-row", section)));
        return cards.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Drags the element from source to target
     *
     * @param source - the source
     * @param target - the target
     * @return current object
     */
    public ComparePage dragDropCard(String source, String target) {
        String byElement = "[data-rbd-drag-handle-draggable-id='%s']";

        WebElement webSource = driver.findElement(By.cssSelector(String.format(byElement, source)));
        WebElement webTarget = driver.findElement(By.cssSelector(String.format(byElement, target)));

        pageUtils.dragAndDrop(webSource, webTarget);
        return this;
    }

    /**
     * Drags the element from source to target
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return current object
     */
    public ComparePage dragDropToBasis(String componentName, String scenarioName) {
        By bySource = By.xpath(String.format("//div[@class='card-header']//div[.='%s / %s']", componentName.trim().toUpperCase(), scenarioName.trim()));
        WebElement byElementSource = pageUtils.waitForElementToAppear(bySource);

        pageUtils.dragAndDrop(byElementSource, basisColumn);
        return this;
    }

    /**
     * Gets card header text
     * This method can be asserted in the following eg. assertThat(comparePage.getCardHeader(), Matchers.containsInRelativeOrder("Info & Notes", "Process"));
     *
     * @return list of string
     */
    public List<String> getCardHeader() {
        List<WebElement> cardHeader = driver.findElements(By.cssSelector(".comparison-column [data-rbd-drag-handle-draggable-id] .section-header"));
        return cardHeader.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Gets basis
     *
     * @return string
     */
    public String getBasis() {
        return pageUtils.waitForElementToAppear(basisColumnHeader).getAttribute("textContent");
    }

    /**
     * Deletes basis
     *
     * @return current page object
     */
    public ComparePage deleteBasis() {
        pageUtils.waitForElementAndClick(deleteBasis);
        return this;
    }

    /**
     * Deletes a comparison
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return current page object
     */
    public ComparePage deleteComparison(String componentName, String scenarioName) {
        By byComparison = By.xpath(String.format("//div[.='%s / %s']/parent::div//div[@class='close-button close-button-dark']", componentName.trim().toUpperCase(), scenarioName.trim()));
        pageUtils.waitForElementAndClick(byComparison);
        return this;
    }

    /**
     * Gets output
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param card          - the card
     * @return string
     */
    public String getOutput(String componentName, String scenarioName, ComparisonCardEnum card) {
        return driver.findElement(By.xpath(String.format("//span[.='%s ']/following-sibling::span[.='/ %s']/../../../../..", componentName, scenarioName)))
            .findElements(By.cssSelector(String.format("[id|='qa-%s'] .left .comparison-row", card.getCardHeader()))).get(card.getCardPosition()).getAttribute("textContent");
    }

    /**
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param card          - the card
     * @param value         - the value
     * @return string
     */
    public boolean isArrowColour(String componentName, String scenarioName, ComparisonCardEnum card, ComparisonDeltaEnum value) {
        return pageUtils.scrollWithJavaScript(getDeltaInfo(componentName, scenarioName, card)
            .findElement(By.cssSelector("svg")), true).getAttribute("color").equals(value.getDelta());
    }

    /**
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param card          - the card
     * @param value         - the value
     * @return string
     */
    public boolean isDeltaIcon(String componentName, String scenarioName, ComparisonCardEnum card, ComparisonDeltaEnum value) {
        return pageUtils.scrollWithJavaScript(getDeltaInfo(componentName, scenarioName, card)
            .findElement(By.cssSelector("svg")), true).getAttribute("data-icon").equals(value.getDelta());
    }

    /**
     * Gets delta percentage
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param card          - the card
     * @return string
     */
    public String getDeltaPercentage(String componentName, String scenarioName, ComparisonCardEnum card) {
        return pageUtils.scrollWithJavaScript(getDeltaInfo(componentName, scenarioName, card), true)
            .getAttribute("textContent");
    }

    /**
     * Gets delta info
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param card          - the card
     * @return webelement
     */
    private WebElement getDeltaInfo(String componentName, String scenarioName, ComparisonCardEnum card) {
        // TODO: 13/05/2021 cf - the xpath below is the only way i can get back to the parent element. the previous locator was much simpler so i sent a message to Jacob asking for it to be reverted
        return driver.findElements(By.xpath(String.format("//span[.='%s ']/following-sibling::span[.='/ %s']/../../../../..//div[contains(@id,'qa-%s')]//div[@class='content']//div[@class='right']/div",
            componentName, scenarioName, card.getCardHeader()))).get(card.getCardPosition());
    }

    /**
     * Opens the comparison
     * @param componentName - the component name
     * @param scenarioName - the scenario name
     * @return new page object
     */
    public EvaluatePage openComparison(String componentName, String scenarioName) {
        By byComparison = By.xpath(String.format("//span[.='%s']/following-sibling::span[.='%s']/ancestor::div[@class='apriori-card dark medium-card card']", componentName.toUpperCase().trim(), scenarioName.trim()));
        pageUtils.waitForElementToAppear(byComparison);
        By byComparisonLink = By.xpath(String.format("//span[.='%s']/following-sibling::span[.='%s']/ancestor::div[@class='apriori-card dark medium-card card']//a", componentName.toUpperCase().trim(), scenarioName.trim()));
        pageUtils.javaScriptClick(driver.findElement(byComparisonLink));
        return new EvaluatePage(driver);
    }
}
