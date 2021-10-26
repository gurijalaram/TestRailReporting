package com.apriori.pageobjects.pages.help;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ZendeskSignInPage extends LoadableComponent<ZendeskSignInPage> {

    @FindBy(css = "input[name='email']")
    private WebElement email;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public ZendeskSignInPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(email);
    }

    /**
     * Switches from parent to child tab
     *
     * @return page object
     */
    public ZendeskSignInPage switchTab() {
        pageUtils.windowHandler(1);
        return this;
    }
}
