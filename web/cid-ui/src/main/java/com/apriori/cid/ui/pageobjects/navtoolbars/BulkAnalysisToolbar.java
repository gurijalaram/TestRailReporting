package com.apriori.cid.ui.pageobjects.navtoolbars;

import com.apriori.cid.ui.pageobjects.bulkanalysis.BulkAnalysisExplorePage;
import com.apriori.cid.ui.pageobjects.bulkanalysis.NewBulkAnalysisModal;
import com.apriori.cid.ui.pageobjects.bulkanalysis.SetInputsModalPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.explore.ImportCadFilePage;
import com.apriori.cid.ui.pageobjects.projects.BulkCostingPage;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class BulkAnalysisToolbar extends MainNavBar {

    private final By refreshLabel = By.xpath("//div[@data-testid='alert-messaging']//div[.='Updating...']");
    @FindBy(css = "[id='qa-input-row-set-inputs'] button")
    protected WebElement setInputsButton;
    @FindBy(css = "[id='qa-worksheet-create-new'] button")
    private WebElement newButton;
    @FindBy(css = "[id='qa-bcm-sub-header-info-button'] button")
    private WebElement infoButton;
    @FindBy(css = "[id='qa-bcm-sub-header-delete-button'] button")
    private WebElement deleteButton;
    @FindBy(css = "[id='qa-sub-header-import-button'] button")
    private WebElement importButton;
    @FindBy(css = "[id='qa-bcm-sub-header-add-button'] button")
    private WebElement addScenariosButton;
    @FindBy(css = "[id='qa-input-row-delete-new'] button")
    private WebElement removeButton;
    @FindBy(css = "[id='qa-bcm-evaluate-page-btn-cost'] button")
    private WebElement costButton;
    @FindBy(css = "[id='qa-bcm-evaluate-page-btn-refresh'] button")
    private WebElement refreshButton;
    @FindBy(css = "[data-testid='bulk-analysis-evaluate'] h3")
    private WebElement bulkAnalysisName;
    @FindBy(css = "[data-testid='bulk-analysis-evaluate'] .secondary-nav-bar-back-text")
    private WebElement allBulkAnalysesButton;
    private PageUtils pageUtils;
    private WebDriver driver;

    public BulkAnalysisToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Click New button
     *
     * @return new page object
     */
    public NewBulkAnalysisModal clickNew() {
        pageUtils.waitForElementAndClick(newButton);
        return new NewBulkAnalysisModal(driver);
    }

    /**
     * Click Info button
     *
     * @return new page object
     */
    public InfoPage clickInfo() {
        pageUtils.waitForElementAndClick(infoButton);
        return new InfoPage(driver);
    }

    /**
     * Click Delete button
     *
     * @return new page object
     */
    public DeletePage clickDelete() {
        pageUtils.waitForElementAndClick(deleteButton);
        return new DeletePage(driver);
    }

    /**
     * Click Delete button
     *
     * @return new page object
     */
    public DeletePage clickRemove() {
        pageUtils.waitForElementAndClick(removeButton);
        return new DeletePage(driver);
    }

    /**
     * Checks if the remove button is enable
     *
     * @return true/false
     */
    public boolean isRemoveButtonEnabled() {
        return pageUtils.isElementEnabled(removeButton);
    }

    /**
     * Clicks the import button
     *
     * @return new page object
     */
    public ImportCadFilePage importCadFile() {
        pageUtils.waitForElementAndClick(importButton);
        return new ImportCadFilePage(driver);
    }

    /**
     * Click Add scenarios button
     *
     * @return new page object
     */
    public BulkAnalysisExplorePage clickAddScenario() {
        pageUtils.waitForElementAndClick(deleteButton);
        return new BulkAnalysisExplorePage(driver);
    }

    /**
     * Click set input button
     *
     * @return new page object
     */
    public SetInputsModalPage clickSetInputs() {
        pageUtils.waitForElementAndClick(setInputsButton);
        return new SetInputsModalPage(driver);
    }

    /**
     * Checks set inputs button is enabled
     * @return true/false
     */
    public boolean isSetInputsEnabled() {
        return pageUtils.waitForElementAppear(setInputsButton).isDisplayed();
    }

    /**
     * Clicks on the Refresh button
     *
     * @return new page object
     */
    public ExplorePage refresh() {
        pageUtils.waitForElementAndClick(refreshButton);
        pageUtils.waitForElementToAppear(refreshLabel);
        pageUtils.waitForElementsToNotAppear(refreshLabel);
        return new ExplorePage(driver);
    }

    /**
     * Click Add scenarios button
     *
     * @return new page object
     */
    public BulkCostingPage clickAllBulkAnalyses() {
        pageUtils.waitForElementAndClick(allBulkAnalysesButton);
        return new BulkCostingPage(driver);
    }

    /**
     * Gets bulk analysis name
     *
     * @return string
     */
    public String getBulkAnalysisName() {
        return pageUtils.waitForElementAppear(bulkAnalysisName).getAttribute("textContent");
    }
}
