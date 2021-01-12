package com.apriori.workflows;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kpatel
 */
public class History extends LoadableComponent<History> {

    private final Logger LOGGER = LoggerFactory.getLogger(History.class);

    @FindBy(css = "div#root_pagemashupcontainer-1_textbox-76 input")
    private WebElement searchJobName;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-81 button")
    private WebElement searchBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_dataexport-99 button")
    private WebElement exportBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-92 button")
    private WebElement viewDetailsBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-62 button")
    private WebElement refreshButton;

    @FindBy(css = "div.objbox tr")
    private List<WebElement> scheduleHistoryListTable;

    private WebDriver driver;
    private PageUtils pageUtils;

    public History(WebDriver driver) {
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
    protected void isLoaded() {
        pageUtils.waitForElementToAppear(searchJobName);
        pageUtils.waitForElementToAppear(searchBtn);
    }
}
