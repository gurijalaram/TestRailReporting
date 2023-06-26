package com.apriori.pageobjects.pages.compare;

import static org.junit.Assert.assertEquals;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.StatusIcon;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ryabsley
 */

public class CreateComparePage extends LoadableComponent<CreateComparePage> {

    private static final Logger logger = LoggerFactory.getLogger(CreateComparePage.class);

    @FindBy(css = ".MuiPaper-root h2")
    private WebElement newComparisonLabel;

    @FindBy(xpath = "//h3[.='Quick Comparison']/../..")
    private WebElement quickComparisonButton;

    @FindBy(xpath = "//h3[.='Manual Comparison']/../..")
    private WebElement manualComparisonButton;

    @FindBy(css = "button[data-testid='secondary-button']")
    private WebElement cancelButton;

    @FindBy(css = "button[data-testid='primary-button']")
    private WebElement createButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private StatusIcon statusIcon;
    private ModalDialogController modalDialogController;


    public CreateComparePage(WebDriver driver) {
        super();
        this.driver = driver;
        this.statusIcon = new StatusIcon(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(newComparisonLabel);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("New Comparison page was not displayed", "New Comparison", pageUtils.waitForElementToAppear(newComparisonLabel).getAttribute("textContent"));
    }

    /**
     * Return if Quick Comparison card is enabled
     *
     * @return Boolean
     */
    public boolean quickComparisonButtonEnabled() {
        return quickComparisonButton.isEnabled();
    }

    /**
     * Return if Manual Comparison card is enabled
     *
     * @return Boolean
     */
    public boolean manualComparisonButtonEnabled() {
        return manualComparisonButton.isEnabled();
    }

    /**
     * Select the Quick Comparison Option
     */
    public CreateComparePage selectQuickComparison() {
        pageUtils.waitForElementAndClick(quickComparisonButton);
        return this;
    }

    /**
     * Select the Manual Comparison Option
     */
    public CreateComparePage selectManualComparison() {
        pageUtils.waitForElementAndClick(manualComparisonButton);
        return this;
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }

    /**
     * Selects the Create button
     *
     * @return new page object
     */
    public ComparePage create() {
        pageUtils.waitForElementAndClick(createButton);
        return new ComparePage(driver);
    }
}
