package pageobjects.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecondaryProcessesPage extends LoadableComponent<SecondaryProcessesPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(SecondaryProcessesPage.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    public SecondaryProcessesPage(WebDriver driver) {
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

    }
}
