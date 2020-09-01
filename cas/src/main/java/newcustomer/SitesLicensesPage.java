package newcustomer;

import com.apriori.utils.PageUtils;

import customeradmin.NavToolbar;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SitesLicensesPage extends LoadableComponent<SitesLicensesPage> {

    private final Logger logger = LoggerFactory.getLogger(SitesLicensesPage.class);

    @FindBy(xpath = "//h5[.='Loaded Licenses']")
    private WebElement licenseHeader;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public SitesLicensesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navToolbar = new NavToolbar(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(licenseHeader);
    }
}
