package pageobjects.pages.evaluate.designguidance;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.common.PanelController;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.help.HelpDocPage;

/**
 * @author cfrith
 */

public class DesignGuidancePage extends LoadableComponent<DesignGuidancePage> {

    private final Logger logger = LoggerFactory.getLogger(DesignGuidancePage.class);

    @FindBy(xpath = "//div[normalize-space(@class)='apriori-table']")
    private WebElement chartTable;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public DesignGuidancePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(chartTable);
    }

    /**
     * Closes current panel
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }

    /**
     * Opens the help page
     * @return new page object
     */
    public HelpDocPage openHelp() {
        return panelController.openHelp();
    }
}
