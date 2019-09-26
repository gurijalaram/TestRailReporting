package com.apriori.pageobjects.pages.evaluate.process;

import com.apriori.pageobjects.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

public class RoutingsPage extends LoadableComponent<RoutingsPage> {

    private final Logger logger = LoggerFactory.getLogger(RoutingsPage.class);

    @FindBy(css = "div[data-ap-comp='routingSelectionTable'] td")
    private WebElement routingTableCell;

    @FindBy(css = "div[data-ap-comp='routingSelectionTable'] td")
    private List<WebElement> routingTableCells;

    @FindBy(css = "label[data-ap-field='lastCostedLabel']")
    private WebElement costedRouting;

    @FindBy(css = "label[data-ap-field='lastSelectedLabel']")
    private WebElement selectedRouting;

    @FindBy(css = "input[data-ap-field='useSelectedRouting']")
    private WebElement routingCheckBox;

    @FindBy(css = "div[data-ap-comp='routingSelectionTable'] div.v-grid-scroller-vertical")
    private WebElement routingScroller;

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
     * @return text as string
     * @param text
     */
    public Boolean getCostedRouting(String text) {
        return pageUtils.checkElementContains(costedRouting, text);
    }

    /**
     * Gets the selected routing text
     * @return text as string
     * @param text
     */
    public Boolean getSelectedRouting(String text) {
        return pageUtils.checkElementContains(selectedRouting, text);
    }

    /**
     * Selects the routing in the table
     * @param routingName - the routing
     * @return routing name as webelement
     */
    public RoutingsPage selectRouting(String routingName) {
        findRouting(routingName).click();
        return this;
    }

    /**
     * Gets the routing details
     * @param routingName - the routing
     * @return current page object
     */
    public RoutingsPage getRouting(String routingName) {
        findRouting(routingName).getText();
        return this;
    }

    /**
     * Gets routings in the table
     * @return list of routings
     */
    public List<String> getRoutings() {
        return routingTableCells.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Finds the routing in the table
     * @param routingName - the routing
     * @return webelement
     */
    public WebElement findRouting(String routingName) {
        By routing = By.xpath("//div[@data-ap-comp='routingSelectionTable']//td[contains(text(),'" + routingName + "')]/ancestor::tr");
        return pageUtils.scrollToElement(routing, routingScroller);
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