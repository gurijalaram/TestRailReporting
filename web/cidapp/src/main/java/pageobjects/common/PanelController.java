package pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.help.HelpDocPage;

/**
 * @author cfrith
 */

public class PanelController extends LoadableComponent<PanelController> {

    private final Logger LOGGER = LoggerFactory.getLogger(PanelController.class);

    @FindBy(css = "svg[data-icon='question']")
    private WebElement questionButton;

    @FindBy(css = "svg[data-icon='times']")
    private WebElement closeButton;

    @FindBy(xpath = "//span[normalize-space(@class)='Resizer Resizer horizontal']")
    private WebElement panelResizer;

    private PageUtils pageUtils;
    private WebDriver driver;

    public PanelController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(questionButton);
        pageUtils.waitForElementAppear(closeButton);
    }

    /**
     * Closes current panel
     * @return new page object
     */
    public EvaluatePage closePanel() {
        pageUtils.waitForElementAndClick(closeButton);
        return new EvaluatePage(driver);
    }

    /**
     * Opens the help page
     * @return new page object
     */
    public HelpDocPage openHelp() {
        pageUtils.waitForElementAndClick(questionButton);
        return new HelpDocPage(driver);
    }
}
