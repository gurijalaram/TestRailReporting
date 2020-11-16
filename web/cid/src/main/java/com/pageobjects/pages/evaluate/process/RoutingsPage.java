package com.pageobjects.pages.evaluate.process;

import com.apriori.utils.PageUtils;
import com.apriori.utils.constants.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

public class RoutingsPage extends LoadableComponent<RoutingsPage> {

    private final Logger logger = LoggerFactory.getLogger(RoutingsPage.class);

    @FindBy(css = "div[data-ap-comp='routingSelectionTable'] td")
    private WebElement routingTableCell;

    @FindBy(css = "label[data-ap-field='lastCostedLabel']")
    private WebElement costedRouting;

    @FindBy(css = "label[data-ap-field='lastSelectedLabel']")
    private WebElement selectedRouting;

    @FindBy(css = "input[data-ap-field='useSelectedRouting']")
    private WebElement routingCheckBox;

    @FindBy(css = "div[data-ap-comp='routingSelectionTable'] div.v-grid-scroller-vertical")
    private WebElement routingScroller;

    @FindBy(css = "div[data-ap-comp='routingSelectionTable'] tr.v-grid-row-has-data")
    private List<WebElement> routingTableRows;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public RoutingsPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(routingTableCell);
    }

    /**
     * Gets the costed routing text
     *
     * @param text
     * @return text as string
     */
    public boolean getCostedRouting(String text) {
        return pageUtils.textPresentInElement(costedRouting, text);
    }

    /**
     * Gets the selected routing text
     *
     * @param text
     * @return text as string
     */
    public boolean getSelectedRouting(String text) {
        return pageUtils.textPresentInElement(selectedRouting, text);
    }

    /**
     * Selects the routing in the table
     *
     * @param routingName - the routing
     * @return routing name as webelement
     */
    public RoutingsPage selectRouting(String routingName) {
        findRouting(routingName).click();
        return this;
    }

    /**
     * Gets the routing details
     *
     * @param routingName - the routing
     * @return current page object
     */
    public RoutingsPage getRouting(String routingName) {
        findRouting(routingName).getText();
        return this;
    }

    /**
     * Gets routings in the table
     *
     * @return list of routings
     */
    public Set<String> getRoutings() {
        Set<String> routingCell = new HashSet<>();

        long startTime = System.currentTimeMillis() / 1000;
        long timeLimitInSeconds = 5;
        Actions scroll = new Actions(driver);

        if (pageUtils.isElementDisplayed(routingScroller)) {
            routingTableRows.forEach(routingRow -> routingCell.add(Arrays.asList(routingRow.getAttribute("innerText").split("\n")).get(0)));
            do {
                scroll.moveToElement(routingScroller).clickAndHold().moveByOffset(0, 50).release().build().perform();
                routingTableRows.forEach(routingRow -> routingCell.add(Arrays.asList(routingRow.getAttribute("innerText").split("\n")).get(0)));
            } while (((System.currentTimeMillis() / 1000) - startTime) < timeLimitInSeconds);
        }
        routingTableRows.forEach(routingRow -> routingCell.add(Arrays.asList(routingRow.getAttribute("innerText").split("\n")).get(0)));

        return routingCell.stream().filter(cell -> !cell.isEmpty()).collect(Collectors.toSet());
    }

    /**
     * Finds the routing in the table
     *
     * @param routingName - the routing
     * @return webelement
     */
    public WebElement findRouting(String routingName) {
        By routing = By.xpath("//div[@data-ap-comp='routingSelectionTable']//td[contains(text(),'" + routingName + "')]/ancestor::tr");
        return pageUtils.scrollToElement(routing, routingScroller, Constants.PAGE_DOWN);
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public ProcessRoutingPage apply() {
        applyButton.click();
        return new ProcessRoutingPage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ProcessRoutingPage cancel() {
        cancelButton.click();
        return new ProcessRoutingPage(driver);
    }

    /**
     * Selects the check box
     *
     * @return new page object
     */
    public RoutingsPage checkRoutingBox() {
        routingCheckBox.click();
        return this;
    }
}