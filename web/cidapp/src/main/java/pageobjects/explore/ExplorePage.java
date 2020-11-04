package pageobjects.explore;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.evaluate.EvaluatePage;
import pageobjects.navtoolbars.ExploreToolbar;

/**
 * @author cfrith
 */

public class ExplorePage extends ExploreToolbar {

    private final Logger LOGGER = LoggerFactory.getLogger(ExplorePage.class);

    @FindBy(xpath = "//button[.='Filters']")
    private WebElement filtersButton;

    @FindBy(css = "button[class='dropdown-toggle btn btn-primary']")
    private WebElement paginatorDropdown;

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement scenarioCount;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ExplorePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementAppear(filtersButton);
    }

    /**
     * Checks filter button is displayed
     *
     * @return visibility of button
     */
    public boolean isFilterButtonPresent() {
        return filtersButton.isDisplayed();
    }

    /**
     * Gets the count of scenarios found
     * @return string
     */
    public String getScenariosFound() {
        return pageUtils.waitForElementAppear(scenarioCount).getText();
    }

    public EvaluatePage openScenario(String partName, String scenarioName) {

        return new EvaluatePage(driver);
    }
}
