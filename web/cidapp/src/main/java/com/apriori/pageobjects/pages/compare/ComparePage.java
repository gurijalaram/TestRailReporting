package com.apriori.pageobjects.pages.compare;

import com.apriori.pageobjects.common.StatusIcon;
import com.apriori.pageobjects.navtoolbars.CompareToolbar;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.StatusIconEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ComparePage extends CompareToolbar {

    private static final Logger logger = LoggerFactory.getLogger(ComparePage.class);

    @FindBy(css = "[data-rbd-droppable-id='header-sections']")
    private WebElement headerSections;

    @FindBy(css = "[placeholder='Description']")
    private WebElement descriptionInput;

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
    public ComparePage dragAndDropCard(String source, String target) {
        String byElement = "[data-rbd-drag-handle-draggable-id='%s']";

        WebElement webSource = driver.findElement(By.cssSelector(String.format(byElement, source)));
        WebElement webTarget = driver.findElement(By.cssSelector(String.format(byElement, target)));

        dragAndDrop(webSource, webTarget);
        return this;
    }

    /**
     * Drags the element from source to target
     *
     * @param source - the source
     * @param target - the target
     * @return current object
     */
    public ComparePage dragAndDropComparison(String source, String target) {
        String[] streamSource = source.split(",");
        String[] streamTarget = target.split(",");

        By byEleS = By.xpath(String.format("//div[@class='card-header']//div[.='%s / %s']", streamSource[0].trim().toUpperCase(), streamSource[1].trim()));
        WebElement byElementSource = pageUtils.waitForElementToAppear(byEleS);
        By byEleT = By.xpath(String.format("//div[@class='card-header']//div[.='%s / %s']", streamTarget[0].trim().toUpperCase(), streamTarget[1].trim()));
        WebElement byElementTarget = pageUtils.waitForElementToAppear(byEleT);

        dragAndDrop(byElementSource, byElementTarget);
        return this;
    }

    /**
     * Drag and drop an element from source to target
     *
     * @param byElementSource - element source
     * @param byElementTarget - element target
     */
    private void dragAndDrop(WebElement byElementSource, WebElement byElementTarget) {
        Actions actions = new Actions(driver);
        actions.clickAndHold(byElementSource)
            .pause(Duration.ofMillis(100))
            .moveByOffset(0, -5)
            .moveByOffset(0, -5)
            .moveToElement(byElementTarget)
            .pause(Duration.ofMillis(500))
            .release()
            .pause(Duration.ofSeconds(1))
            .perform();
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
     * Gets comparison header text
     * This method can be asserted in the following eg. assertThat(comparePage.getCardHeader(), Matchers.containsInRelativeOrder("BRACKET_BASIC / Initial", "PUSH PIN / Initial"));
     *
     * @return list of string
     */
    public List<String> getComparisonHeader() {
        List<WebElement> cardHeader = driver.findElements(By.cssSelector(".comparison-column.draggable .apriori-card .card-header"));
        return cardHeader.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
    }
}
