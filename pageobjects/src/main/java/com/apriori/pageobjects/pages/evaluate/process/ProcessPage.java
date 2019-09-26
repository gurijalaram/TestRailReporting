package com.apriori.pageobjects.pages.evaluate.process;

import com.apriori.pageobjects.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessPage extends LoadableComponent<ProcessPage> {

    private final Logger logger = LoggerFactory.getLogger(ProcessPage.class);

    @FindBy(css = "div[data-ap-field='machineVisible']")
    private WebElement processInfo;

    @FindBy(css = "div[data-ap-comp='costingResult']")
    private WebElement processContributions;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ProcessPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(processInfo);
    }

    /**
     * Gets the process info
     * @param text - the text
     * @return true/false
     */
    public Boolean getProcessInfo(String text) {
        return pageUtils.checkElementAttribute(processInfo, "innerText", text);
    }

    /**
     * Gets the process contribution
     * @param text - the text
     * @return true/false
     */
    public Boolean getProcessContributions(String text) {
        return pageUtils.checkElementAttribute(processContributions, "innerText", text);
    }
}
