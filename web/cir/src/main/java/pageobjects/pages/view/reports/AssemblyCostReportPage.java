package pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblyCostReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(AssemblyCostReportPage.class);

    @FindBy(xpath = "//label[@title='Single export set selection.']/div/div/div/a")
    private WebElement exportSetDropdown;

    @FindBy(xpath = "//label[@title='Single [assembly] part number selection.']/div/div/div/a")
    private WebElement assemblyPartNumberDropdown;

    @FindBy(xpath = "//label[@title='Single scenario name selection.']/div/div/div/a")
    private WebElement scenarioNameDropdown;

    @FindBy(xpath = "//li[@title='TOP-LEVEL']/..")
    private WebElement assemblyPartNumberDropdownItemList;

    @FindBy(xpath = "//li[@title='Initial']/..")
    private WebElement scenarioNameItemList;

    private String dropdownOptionLocator = "li[title='%s'] > div > a";

    private PageUtils pageUtils;
    private WebDriver driver;

    public AssemblyCostReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Selects Export Set from Dropdown
     * @param exportSet String
     * @param className class to return instance of
     * @param <T> generic
     * @return instance of class
     */
    public <T> T selectExportSetDropdown(String exportSet, Class<T> className) {
        if (!exportSetDropdown.getAttribute("title").equals(exportSet)) {
            pageUtils.waitForElementAndClick(exportSetDropdown);
            By locator = By.cssSelector(String.format(dropdownOptionLocator, exportSet));
            pageUtils.waitForElementAndClick(locator);
        }
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects Assembly from Dropdown
     * @param assemblyName String
     * @param className class to return instance of
     * @param <T> generic
     * @return instance of class
     */
    public <T> T selectAssemblySetDropdown(String assemblyName, Class<T> className) {
        if (!assemblyPartNumberDropdown.getAttribute("title").equals(assemblyName)) {
            pageUtils.waitForElementAndClick(assemblyPartNumberDropdown);
            By locator = By.cssSelector(String.format(dropdownOptionLocator, assemblyName));
            pageUtils.waitForElementAndClick(locator);
        }
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects Assembly from Dropdown
     * @param scenarioName String
     * @param className class to return instance of
     * @param <T> generic
     * @return instance of class
     */
    public <T> T selectScenarioNameDropdown(String scenarioName, Class<T> className) {
        if (!scenarioNameDropdown.getAttribute("title").equals(scenarioName)) {
            pageUtils.waitForElementAndClick(scenarioNameDropdown);
            By locator = By.cssSelector(String.format(dropdownOptionLocator, scenarioName));
            pageUtils.waitForElementAndClick(locator);
        }
        return PageFactory.initElements(driver, className);
    }

    /**
     * Waits for Assembly Part Number filter to occur
     * @return instance of current page object
     */
    public AssemblyCostReportPage waitForAssemblyPartNumberFilter() {
        By locator = By.xpath("//a[@title='TOP-LEVEL']");
        pageUtils.waitForElementToAppear(locator);
        return this;
    }

    public String getAssemblyPartNumberFilterItemCount() {
        return assemblyPartNumberDropdownItemList.getAttribute("childElementCount");
    }

    public String getScenarioNameCount() {
        return scenarioNameItemList.getAttribute("childElementCount");
    }

    public boolean isAssemblyPartNumberItemEnabled(String itemName) {
        return driver.findElement(By.cssSelector(String.format(dropdownOptionLocator, itemName))).isEnabled();
    }

    public boolean isScenarioNameEnabled(String scenarioName) {
        return driver.findElement(By.cssSelector(String.format(dropdownOptionLocator, scenarioName))).isEnabled();
    }
}
