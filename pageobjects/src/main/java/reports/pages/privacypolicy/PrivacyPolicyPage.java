package reports.pages.privacypolicy;

import com.apriori.pageobjects.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reports.pages.login.LoginPage;

public class PrivacyPolicyPage extends LoadableComponent<PrivacyPolicyPage> {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    @FindBy(xpath = "//section[@id='services_title_section']/div/h1")
    private WebElement privacyPolicyTitle;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PrivacyPolicyPage(WebDriver driver) {
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
     * Gets window url
     * @return - string
     */
    public String getChildWindowURL() {
        return pageUtils.windowHandler().getCurrentUrl();
    }

    /**
     * Gets number of open tabs
     * @return int - number of tabs
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }
}
