package reports.pages.view;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reports.pages.login.LoginPage;

public class SearchResults extends LoadableComponent<SearchResults> {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "body")
    private WebElement searchResultsPageTitle;

    public SearchResults(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Get page title text
     * @return String - page title text
     */
    public String getSearchResultsTitleText() {
        pageUtils.waitForElementToAppear(searchResultsPageTitle);
        return searchResultsPageTitle.getAttribute("id");
    }
}
