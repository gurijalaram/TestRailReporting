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

    @FindBy(xpath = "//li[@title='TOP-LEVEL']/..")
    private WebElement assemblyPartNumberDropdownItemList;

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
     */
    public <T> T selectExportSetDropdown(String exportSet, Class<T> className) {
        pageUtils.waitForElementAndClick(exportSetDropdown);
        if (!exportSetDropdown.getAttribute("title").equals(exportSet)) {
            By locator = By.cssSelector(String.format("li[title='%s'] > div > a", exportSet));
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
        pageUtils.waitForElementToAppear(assemblyPartNumberDropdownItemList);
        return assemblyPartNumberDropdownItemList.getAttribute("childElementCount");
    }

    public boolean isAssemblyPartNumberItemDisplayedAndEnabled(String itemName) {
        WebElement elementToUse = driver.findElement(By.xpath(String.format("//li[@title='%s']/div/a", itemName)));
        pageUtils.waitForElementToAppear(elementToUse);
        return elementToUse.isEnabled() && elementToUse.isDisplayed();
    }
}
