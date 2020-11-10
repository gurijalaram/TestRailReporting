package pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.common.PanelController;
import pageobjects.pages.help.HelpDocPage;

/**
 * @author cfrith
 */

public class ProcessesPage extends LoadableComponent<ProcessesPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(ProcessesPage.class);

    @FindBy(css = "div[dir='ltr']")
    private WebElement chartContainer;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public ProcessesPage(WebDriver driver) {
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
        pageUtils.waitForElementAppear(chartContainer);
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
