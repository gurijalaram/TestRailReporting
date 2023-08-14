package com.apriori.common;

import com.apriori.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class AddCustomPartPage extends LoadableComponent<AddCustomPartPage> {

    @FindBy(css = ".modal-title .bill-of-materials-type")
    private WebElement pcbaLogo;

    private PageUtils pageUtils;
    private WebDriver driver;

    public AddCustomPartPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(pcbaLogo);
    }
}
