package com.apriori.pageobjects.pages.compare;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ComparePage extends LoadableComponent<ComparePage> {

    private final Logger logger = LoggerFactory.getLogger(ComparePage.class);

    @FindBy(css = "table.comparison-table-header-widget-table")
    private WebElement scenarioTable;

    @FindBy(css = "div.gwt-Label.comparison-table-header-part-number")
    private WebElement comparisonName;

    @FindBy(css = "textarea.gwt-TextArea.full-width")
    private WebElement descriptionText;

    @FindBy(css = "button.comparison-table-add-scenarios-button")
    private WebElement addScenariosButton;

    @FindBy(css = "[class='locked-status-icon fa fa-lock']")
    private WebElement lockedIcon;

    @FindBy(css = "[class='locked-status-icon fa fa-unlock']")
    private WebElement unlockedIcon;

    @FindBy(css = "[data-ap-comp='loadingComparisonData']")
    private WebElement loadingComparisonData;


    private WebDriver driver;
    private PageUtils pageUtils;

    public ComparePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(scenarioTable);
    }

    /**
     * Adds a new scenario
     *
     * @return new page object
     */
    public ComparisonTablePage addScenario() {
        pageUtils.waitForElementAndClick(addScenariosButton);
        return new ComparisonTablePage(driver);
    }

    /**
     * Gets the comparison description text
     *
     * @return the text as String
     */
    public String getDescriptionText() {
        return descriptionText.getText();
    }

    /**
     * Checks the comparison name
     * @param text - the text
     * @return true/false
     */
    public boolean isComparisonName(String text) {
        return pageUtils.checkElementAttribute(comparisonName, "title", text);
    }

    /**
     * Checks if the comparison is locked
     * @param text - the text
     * @return true/false
     */
    public boolean isComparisonLocked(String text) {
        return pageUtils.checkElementAttribute(lockedIcon, "title", text);
    }

    /**
     * Checks if the comparison is unlocked
     * @param text - the text
     * @return true/false
     */
    public boolean isComparisonUnlocked(String text) {
        return pageUtils.checkElementAttribute(unlockedIcon, "title", text);
    }

    /**
     * Checks if the comparison is being updated
     * @param text - the text in the attribute
     * @return current page object
     */
    public ComparePage checkComparisonUpdated(String text) {
        pageUtils.checkElementAttribute(loadingComparisonData, "style", text);
        return this;
    }
}
