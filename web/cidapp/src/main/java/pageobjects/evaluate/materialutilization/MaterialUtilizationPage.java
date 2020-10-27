package pageobjects.evaluate.materialutilization;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.common.PanelController;
import pageobjects.evaluate.EvaluatePage;

public class MaterialUtilizationPage extends LoadableComponent<MaterialUtilizationPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(MaterialUtilizationPage.class);

    @FindBy(xpath = "//div[contains(@class,'apriori-card tabbed')]")
    private WebElement panelDetails;

    private PageUtils pageUtils;
    private WebDriver driver;
    private PanelController panelController;

    public MaterialUtilizationPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(panelDetails);
    }

    /**
     * Gets material information
     *
     * @param materialInfo
     * @return string
     */
    public String getUtilizationInfo(String materialInfo) {
        By info = By.xpath(String.format("//span[.='%s']/..//span[@class='property-value']", materialInfo));
        return pageUtils.waitForElementToAppear(info).getAttribute("textContext");
    }

    /**
     * Closes current panel
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }
}
