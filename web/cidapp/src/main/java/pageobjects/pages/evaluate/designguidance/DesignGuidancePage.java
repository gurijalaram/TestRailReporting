package pageobjects.pages.evaluate.designguidance;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesignGuidancePage extends LoadableComponent<DesignGuidancePage> {

    private final Logger logger = LoggerFactory.getLogger(DesignGuidancePage.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    public DesignGuidancePage(WebDriver driver) {
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
