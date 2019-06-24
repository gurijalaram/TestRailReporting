package main.java.pages.evaluate.materialutilization;

import main.java.pages.evaluate.materialutilization.stock.StockPage;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaterialPage extends LoadableComponent<MaterialPage> {

    private final Logger logger = LoggerFactory.getLogger(MaterialUtilizationPage.class);

    @FindBy(css = "div[data-ap-comp='materialDetails']")
    private WebElement materialTable;

    @FindBy(css = "a[href='#materialDetailsTab']")
    private WebElement materialUtilizationTab;

    @FindBy(css = "a[href='#stockDetailsTab']")
    private WebElement stockTab;

    @FindBy(css = "a[href='#partNestingTab']")
    private WebElement partNestingTab;

    private WebDriver driver;
    private PageUtils pageUtils;

    public MaterialPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(materialTable);
    }

    /**
     * Opens the material & utilization tab
     * @return new page object
     */
    public MaterialUtilizationPage goToMaterialUtilizationTab() {
        materialUtilizationTab.click();
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Opens the stock tab
     * @return new page object
     */
    public StockPage goToStockTab() {
        stockTab.click();
        return new StockPage(driver);
    }
}
