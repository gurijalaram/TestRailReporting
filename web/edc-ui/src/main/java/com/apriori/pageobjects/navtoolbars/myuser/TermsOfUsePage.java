package com.apriori.pageobjects.navtoolbars.myuser;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class TermsOfUsePage extends LoadableComponent<TermsOfUsePage> {

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public TermsOfUsePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
