package com.apriori.pageobjects.pages.compare;

import com.apriori.pageobjects.common.StatusIcon;
import com.apriori.pageobjects.navtoolbars.CompareToolbar;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.StatusIconEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public ComparePage getCardInfo(String card, String componentName, String scenarioName) {
        List<WebElement> byCard = driver.findElements(By.cssSelector("[data-rbd-droppable-id='header-sections'] .comparison-row"));
        List<WebElement> byValue = driver.findElement(By.xpath(String.format("//div[.='%s / %s']/parent::div", componentName, scenarioName))).findElements(By.cssSelector(".comparison-row"));

        int cardIndex = IntStream.range(0, byCard.size()).filter(x -> byCard.get(x).getText().equals(card)).findFirst().getAsInt();

        String valueText = byValue.get(cardIndex).getAttribute("textContent");

        List<WebElement> byIndex = driver.findElements(By.xpath(String.format("//div[.='%s / %s']/parent::div//div[.='%s']/parent::div//div[@class='comparison-row']", componentName, scenarioName, valueText)));

        int positionIndex = IntStream.range(0, byIndex.size()).filter(x -> byIndex.get(x).getText().equals(valueText)).findFirst().getAsInt();

        positionIndex = positionIndex > 0 ? positionIndex + 1 : positionIndex;

        String arrowColour = driver.findElements(By.xpath(String.format("//div[.='%s / %s']/parent::div//div[.='%s']/parent::div//div[@class='comparison-row']/parent::div/..//div[@class='right']//div", componentName, scenarioName, valueText))).get(positionIndex).findElement(By.cssSelector("svg")).getAttribute("color");
        String funnyColumnText = null;

        if (positionIndex > 0) {
            funnyColumnText = driver.findElements(By.xpath(String.format("//div[.='%s / %s']/parent::div//div[.='%s']/parent::div//div[@class='comparison-row']/parent::div/..//div[@class='right']//div", componentName, scenarioName, valueText))).get(positionIndex).findElement(By.cssSelector(".property-value")).getText();
        }
        return this;
    }
}
