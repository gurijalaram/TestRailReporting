package com.apriori.pageobjects.pages.evaluate.materialprocess;

import com.apriori.pageobjects.common.PanelController;
import com.apriori.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartNestingPage extends LoadableComponent<StockPage> {

    private static final Logger logger = LoggerFactory.getLogger(StockPage.class);

    @FindBy(css = ".tab.active [class='tab']")
    private WebElement partNestingTabActive;

    @FindBy(xpath = "//div[.='Dimensions']")
    private WebElement panelHeading;

    private PageUtils pageUtils;
    private WebDriver driver;
    private PanelController panelController;

    public PartNestingPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(partNestingTabActive);
        pageUtils.waitForElementAppear(panelHeading);
    }
}
