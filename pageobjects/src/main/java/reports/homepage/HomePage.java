package reports.homepage;

import com.apriori.pageobjects.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends LoadableComponent<HomePage> {

    private final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    public HomePage(WebDriver driver){
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
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
