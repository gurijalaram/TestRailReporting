package com.apriori.pageobjects.pages.create;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateAdHocViewPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(CreateAdHocViewPage.class);

    @FindBy(xpath = "//div[@id='display']/div[2]/div/div[1]/div")
    private WebElement adHocViewPageTitle;

    @FindBy(xpath = "//div[@class='pageHeader-title-text']")
    private WebElement adHocViewCreationPageTitle;

    @FindBy(xpath = "//div[contains(@class, 'sourceDialogNew ')]")
    private WebElement adHocViewDialog;

    @FindBy(xpath = "(//li[@class='leaf domain view11'])[1]")
    private WebElement adHocViewDataSourceOne;

    @FindBy(xpath = "//span[contains(text(), 'Choose Data...')]")
    private WebElement chooseDataButton;

    @FindBy(xpath = "(//p[@class='wrap button draggable'])[3]")
    private WebElement chooseDataFirstOption;

    @FindBy(id = "toRight")
    private WebElement moveDataToRightButton;

    @FindBy(id = "goToDesigner")
    private WebElement goToDesignerOkButton;

    @FindBy(id = "changeVisualizationButton")
    private WebElement changeVisualizationButton;

    @FindBy(xpath = "//li[@data-chart-type='Table']")
    private WebElement visualizationTableOptionButton;

    @FindBy(xpath = "//span[contains(text(), 'Apply and Close')]")
    private WebElement applyAndCloseVisualizationButton;

    @FindBy(xpath = "(//li[@class='folders node closed'])[1]")
    private WebElement firstDataField;

    private final PageUtils pageUtils;
    private final WebDriver driver;

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
     * Get page title text
     *
     * @return String - page title text
     */
    public String getAdHocViewTitleText() {
        pageUtils.waitForElementToAppear(adHocViewPageTitle);
        return adHocViewPageTitle.getText();
    }

    /**
     * Gets dialog isDisplayed value
     *
     * @return boolean - isDisplayed
     */
    public boolean isDialogDisplayed() {
        pageUtils.waitForElementToAppear(adHocViewDialog);
        return adHocViewDialog.isDisplayed();
    }

    /**
     * Gets dialog isEnabled value
     *
     * @return boolean - is Enabled
     */
    public boolean isDialogEnabled() {
        pageUtils.waitForElementToAppear(adHocViewDialog);
        return adHocViewDialog.isEnabled();
    }

    /**
     * Checks if create ad hoc view page is displayed and enabled
     *
     * @return boolean
     */
    public boolean isCreateAdHocViewPageDisplayedAndEnabled() {
        return adHocViewCreationPageTitle.isEnabled() && adHocViewCreationPageTitle.isDisplayed();
    }

    /**
     * Click first data source
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage clickFirstDataSource() {
        pageUtils.waitForElementAndClick(adHocViewDataSourceOne);
        return this;
    }

    /**
     * Click choose data button
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage clickChooseData() {
        pageUtils.waitForElementAndClick(chooseDataButton);
        return this;
    }

    /**
     * Wait for choose data dialog data to appear
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage waitForChooseDataDialogToAppear() {
        pageUtils.waitForElementToAppear(chooseDataFirstOption);
        return this;
    }

    /**
     * Click move data to right button
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage clickMoveDataToRightButton() {
        pageUtils.waitForElementAndClick(moveDataToRightButton);
        return this;
    }

    /**
     * Click go to designer
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage clickGoToDesignerOkButton() {
        pageUtils.waitForElementAndClick(goToDesignerOkButton);
        isCreateAdHocViewPageDisplayedAndEnabled();
        return this;
    }

    /**
     * Change visualization to table
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage changeVisualizationToTable() {
        pageUtils.waitForElementToAppear(firstDataField);
        pageUtils.waitForElementAndClick(changeVisualizationButton);
        pageUtils.waitForElementAndClick(visualizationTableOptionButton);
        pageUtils.waitForElementAndClick(applyAndCloseVisualizationButton);
        return this;
    }

    /**
     * Change data scope to full
     *
     * @return CreateAdHocViewPage instance
     */
    public CreateAdHocViewPage changeDataScopeToFull() {
        Select dataScope = new Select(driver.findElement(By.id("dataSizeSelector")));
        dataScope.selectByValue("full");
        return this;
    }

    public CreateAdHocViewPage addDataToTable() {
        Actions actions = new Actions(driver);

        // add Scenario Part Number
        pageUtils.waitForElementAndClick(By.xpath("(//div[@id='fields']//input)[1]"));

        driver.findElement(By.xpath("(//div[@id='fields']//input)[1]")).clear();
        driver.findElement(By.xpath("(//div[@id='fields']//input)[1]")).sendKeys("Scenario Part Number");
        driver.findElement(By.xpath("(//div[@id='fields']//input)[1]")).sendKeys(Keys.ENTER);

        pageUtils.waitForElementToAppear(By.xpath("(//ul[@id='dimensionsTree']//li)[1]"));

        actions.contextClick(
                driver.findElement(
                    By.xpath("(//ul[@id='dimensionsTree']//li)[1]")))
            .build()
            .perform();
        pageUtils.waitForElementAndClick(By.xpath("//ul[@id='menuList']/li[1]"));

        // add Scenario Name
        pageUtils.waitForElementAndClick(By.xpath("(//div[@id='fields']//input)[1]"));

        driver.findElement(By.xpath("(//div[@id='fields']//input)[1]")).clear();
        driver.findElement(By.xpath("(//div[@id='fields']//input)[1]")).sendKeys("Scenario Name");
        driver.findElement(By.xpath("(//div[@id='fields']//input)[1]")).sendKeys(Keys.ENTER);

        pageUtils.waitForElementToAppear(By.xpath("(//ul[@id='dimensionsTree']//li)[3]"));

        actions.contextClick(
                driver.findElement(
                    By.xpath("(//ul[@id='dimensionsTree']//li)[3]")))
            .build()
            .perform();
        pageUtils.waitForElementAndClick(By.xpath("//ul[@id='menuList']/li[1]"));

        // add scenario fbc to table
        pageUtils.waitForElementAndClick(By.xpath("(//div[@id='fields']//input)[2]"));

        driver.findElement(By.xpath("(//div[@id='fields']//input)[2]")).clear();
        driver.findElement(By.xpath("(//div[@id='fields']//input)[2]"))
            .sendKeys("Scenario Fully Burdened Cost");
        driver.findElement(By.xpath("(//div[@id='fields']//input)[2]")).sendKeys(Keys.ENTER);

        pageUtils.waitForElementToAppear(By.xpath("(//ul[@id='measuresTree']//li)[1]"));

        actions.contextClick(
                driver.findElement(
                    By.xpath("(//ul[@id='measuresTree']//li)[2]")))
            .build()
            .perform();
        pageUtils.waitForElementAndClick(By.xpath("//ul[@id='menuList']/li[1]"));

        return this;
    }

    public CreateAdHocViewPage addFilterToTable() {
        Actions actions = new Actions(driver);
        pageUtils.waitForElementAndClick(By.xpath("(//div[@id='fields']//input)[1]"));

        driver.findElement(By.xpath("(//div[@id='fields']//input)[1]")).clear();
        driver.findElement(By.xpath("(//div[@id='fields']//input)[1]")).sendKeys("Latest Export Set Name");
        driver.findElement(By.xpath("(//div[@id='fields']//input)[1]")).sendKeys(Keys.ENTER);

        pageUtils.waitForElementToAppear(By.xpath("(//ul[@id='dimensionsTree']//li)[1]"));

        actions.contextClick(
                driver.findElement(
                    By.xpath("(//ul[@id='dimensionsTree']//li)[1]")))
            .build()
            .perform();
        pageUtils.waitForElementAndClick(By.xpath("//ul[@id='menuList']/li[3]"));

        pageUtils.waitForElementAndClick(By.xpath("//div[@id='filters']//a"));
        pageUtils.waitForElementAndClick(By.xpath("//div[@id='filters']//a/following-sibling::div/input"));
        driver.findElement(By.xpath("//div[@id='filters']//a/following-sibling::div/input")).clear();
        driver.findElement(By.xpath("//div[@id='filters']//a/following-sibling::div/input")).sendKeys("top-level");

        pageUtils.waitForElementAndClick(By.xpath("//li[@title='---01-top-level']"));
        pageUtils.waitForElementToAppear(By.xpath("//a[@title='---01-top-level']"));

        pageUtils.waitForElementAndClick(By.xpath("//fieldset[@id='applyFilter']/button"));

        pageUtils.waitForElementToAppear(By.xpath("//tbody[@id='tableDetails']/tr[1]/td[1]/span[contains(text(), '3538968')]"));

        return this;
    }
}
