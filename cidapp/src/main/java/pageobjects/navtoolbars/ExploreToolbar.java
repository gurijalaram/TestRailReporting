package pageobjects.navtoolbars;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ExploreToolbar extends MainNavBar {

    private final Logger LOGGER = LoggerFactory.getLogger(ExploreToolbar.class);

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

    public ExploreToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementAppear(newButton);
        pageUtils.waitForElementAppear(publishButton);
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
