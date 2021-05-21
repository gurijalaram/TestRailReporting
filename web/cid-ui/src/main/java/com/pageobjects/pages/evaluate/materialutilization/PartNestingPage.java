package com.pageobjects.pages.evaluate.materialutilization;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.toolbars.EvaluatePanelToolbar;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author smccaffrey
 */

public class PartNestingPage extends EvaluatePanelToolbar {

    private static final Logger logger = LoggerFactory.getLogger(PartNestingPage.class);

    @FindBy(css = "label[data-ap-field='selectedSheet']")
    private WebElement selectedSheet;

    @FindBy(css = "label[data-ap-field='blankSize']")
    private WebElement blankSize;

    @FindBy(css = "label[data-ap-field='partsPerSheet']")
    private WebElement partsPerSheet;

    @FindBy(css = "input[data-ap-comp='COMPUTED']")
    private WebElement rectangularNesting;

    @FindBy(css = "input[data-ap-comp='TRUE_PART']")
    private WebElement truePartNesting;

    @FindBy(css = "input[data-ap-comp='MACHINE_DEFAULT']")
    private WebElement machineDefaultPartNesting;

    @FindBy(css = "div[data-ap-comp='materialNestingDiagram']")
    private WebElement partNestingDiagram;

    @FindBy(css = ".panel .glyphicon-remove")
    private WebElement closePanelButton;

    @FindBy(css = "button[data-ap-comp='expandPanelButton']")
    private WebElement chevronButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PartNestingPage(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementToAppear(selectedSheet);
    }

    /**
     * Selects Rectangular radio button
     *
     * @return current page object
     */
    public PartNestingPage selectRectangularNesting() {
        pageUtils.waitForElementAndClick(rectangularNesting);
        return this;
    }

    /**
     * Selects True-Part Shape radio button
     *
     * @return current page object
     */
    public PartNestingPage selectTrue_PartShapeNesting() {
        pageUtils.waitForElementAndClick(truePartNesting);
        return this;
    }

    /**
     * Selects Machine Default radio button
     *
     * @return current page object
     */
    public PartNestingPage selectMachineDefaultNesting() {
        pageUtils.waitForElementAndClick(machineDefaultPartNesting);
        return this;
    }

    /**
     * Closes the material & utilization
     *
     * @return new page object
     */
    public EvaluatePage closePartNestingPanel() {
        pageUtils.waitForElementAndClick(closePanelButton);
        return new EvaluatePage(driver);
    }

    /**
     * Gets Selected Stock Sheet
     *
     * @return string
     */
    public String getSelectedSheet() {
        return selectedSheet.getText();
    }

    /**
     * Gets computed Blank Size
     *
     * @return string
     */
    public String getBlankSize() {
        return blankSize.getText();
    }

    /**
     * Gets Number of Parts Per Sheet
     *
     * @return string
     */
    public String getPartsPerSheet() {
        return partsPerSheet.getText();
    }

    /**
     * Check Rectangular Nesting is selected
     *
     * @return current page object
     */
    public String isRectangularNesting(String attribute) {
        return pageUtils.waitForElementToAppear(rectangularNesting).getAttribute(attribute);
    }

    /**
     * Check Rectangular Nesting is selected
     *
     * @return current page object
     */
    public String isTruePartNesting(String attribute) {
        return pageUtils.waitForElementToAppear(truePartNesting).getAttribute(attribute);
    }

    /**
     * Check Rectangular Nesting is selected
     *
     * @return current page object
     */
    public String isMachineDefaultNesting(String attribute) {
        return pageUtils.waitForElementToAppear(machineDefaultPartNesting).getAttribute(attribute);
    }

    /**
     * Expands the panel
     *
     * @return current page object
     */
    public PartNestingPage expandPanel() {
        pageUtils.waitForElementAndClick(chevronButton);
        return this;
    }
}