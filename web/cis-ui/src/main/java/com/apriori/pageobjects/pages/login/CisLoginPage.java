package com.apriori.pageobjects.pages.login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class CisLoginPage extends LoadableComponent<CisLoginPage> {

    private static String loginPageUrl = PropertiesContext.get("${env}.cis.ui_url");

    private WebDriver driver;
    private PageUtils pageUtils;

    public CisLoginPage(WebDriver driver) {
        init(driver, "", true);
    }

    public CisLoginPage(WebDriver driver, String url) {
        init(driver, url, true);
    }

    public CisLoginPage(WebDriver driver, boolean loadNewPage) {
        init(driver, "", loadNewPage);
    }

    public void init(WebDriver driver, String url, boolean loadNewPage) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        if (url == null || url.isEmpty()) {
            url = loginPageUrl;
        }
        if (loadNewPage) {
            driver.get(url);
        }
        log.info("CURRENTLY ON INSTANCE: " + url);
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