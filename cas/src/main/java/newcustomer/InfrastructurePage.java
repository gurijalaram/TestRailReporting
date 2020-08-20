package newcustomer;

import com.apriori.utils.PageUtils;

import customeradmin.NavToolbar;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfrastructurePage extends LoadableComponent<InfrastructurePage> {

    private final Logger logger = LoggerFactory.getLogger(InfrastructurePage.class);

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public InfrastructurePage(WebDriver driver) {
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

    }
}
