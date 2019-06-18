package main.java.pages.evaluate.designguidance.tolerances;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TolerancePage extends LoadableComponent<TolerancePage> {

    private final Logger logger = LoggerFactory.getLogger(TolerancePage.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    public TolerancePage(WebDriver driver) {
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
