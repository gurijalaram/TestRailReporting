package navtoolbars;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ExploreBar extends LoadableComponent<ExploreBar> {

    private final Logger LOGGER = LoggerFactory.getLogger(ExploreBar.class);

    @FindBy(xpath = "//button[.='New']")
    private WebElement newButton;

    @FindBy(xpath = "//button[.='Publish']")
    private WebElement publishButton;

    @FindBy(xpath = "//button[.='Revert']")
    private WebElement revertButton;

    @FindBy(xpath = "//button[.='Delete']")
    private WebElement deleteButton;

    @FindBy(xpath = "//button[.='Actions']")
    private WebElement actionsButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ExploreBar(WebDriver driver) {
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
        pageUtils.waitForElementAppear(newButton);
        pageUtils.waitForElementAppear(actionsButton);
    }

    /**
     * Checks delete button is displayed
     *
     * @return visibility of button
     */
    public boolean isDeleteButtonPresent() {
        return deleteButton.isDisplayed();
    }
}
