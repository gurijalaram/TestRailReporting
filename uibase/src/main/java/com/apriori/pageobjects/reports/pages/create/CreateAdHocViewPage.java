package com.apriori.pageobjects.reports.pages.create;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateAdHocViewPage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(CreateAdHocViewPage.class);

    @FindBy(xpath = "//div[@id='display']/div[2]/div/div[1]/div")
    private WebElement adHocViewPageTitle;

    @FindBy(xpath = "//div[contains(@class, 'sourceDialogNew ')]")
    private WebElement adHocViewDiaolog;

    @FindBy(xpath = "//ul[@class='list collapsible']//p[contains(text(), 'aPriori Ad Hoc Data 20.1.0.0_SNAPSHOT')]/..")
    private WebElement adHocViewDataSourceButton;

    @FindBy(xpath = "//span[contains(text(), 'Choose Data...')]/..")
    private WebElement chooseDataButton;

    @FindBy(css = "h2")
    private WebElement chooseDataHeading;

    @FindBy(xpath = "//ul[@id='sourceFieldsTree']//p[contains(text(), 'All Latest Scenarios')]/..")
    private WebElement firstSourceOptionLeft;

    @FindBy(id = "toRight")
    private WebElement toRightButton;

    @FindBy(xpath = "//ul[@id='destinationFieldsTree']//p[contains(text(), 'All Latest Scenarios')]/..")
    private WebElement firstSourceOptionRight;

    @FindBy(id = "goToDesigner")
    private WebElement goToDesignerButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public CreateAdHocViewPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Click data source in ad hoc view
     * @return Instance of CreateAdHocViewPage
     */
    public CreateAdHocViewPage clickDataSource() {
        pageUtils.waitForElementAndClick(adHocViewDataSourceButton);
        pageUtils.waitForElementAndClick(chooseDataButton);
        pageUtils.waitForElementToAppear(chooseDataHeading);
        pageUtils.waitForElementToAppear(firstSourceOptionLeft);
        return this;
    }

    /**
     * Moves all data sources to right
     * @return Instance of CreateAdHocViewPage
     */
    public CreateAdHocViewPage moveAllToRight() {
        pageUtils.waitForElementAndClick(toRightButton);
        pageUtils.waitForElementToAppear(firstSourceOptionRight);
        return this;
    }

    /**
     * Go to Designer
     * @return Instance of CreateAdHocViewPage
     */
    public CreateAdHocViewDesignerPage goToDesigner() {
        pageUtils.waitForElementAndClick(goToDesignerButton);
        return new CreateAdHocViewDesignerPage(driver);
    }

    /**
     * Get page title text
     *
     * @return String - page title text
     */
    public String getAdHocViewTitleText() {
        pageUtils.waitForElementToAppear(adHocViewPageTitle);
        return adHocViewPageTitle.getText();
    }

    /**
     * ]
     * Gets dialog isDisplayed value
     *
     * @return boolean - isDisplayed
     */
    public boolean isDialogDisplayed() {
        pageUtils.waitForElementToAppear(adHocViewDiaolog);
        return adHocViewDiaolog.isDisplayed();
    }

    /**
     * Gets dialog isEnabled value
     *
     * @return boolean - is Enabled
     */
    public boolean isDialogEnabled() {
        pageUtils.waitForElementToAppear(adHocViewDiaolog);
        return adHocViewDiaolog.isEnabled();
    }
}
