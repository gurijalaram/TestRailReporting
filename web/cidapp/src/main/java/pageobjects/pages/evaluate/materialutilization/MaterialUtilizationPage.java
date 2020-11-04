package pageobjects.pages.evaluate.materialutilization;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class MaterialUtilizationPage extends LoadableComponent<MaterialUtilizationPage> {

    private final Logger logger = LoggerFactory.getLogger(MaterialUtilizationPage.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    public MaterialUtilizationPage(WebDriver driver) {
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
