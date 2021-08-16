package com.apriori.pageobjects.navtoolbars.myuser;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class TermsOfUsePage {

    @FindBy(xpath = "//p[@class='main-text']")
    private WebElement termsText;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public TermsOfUsePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets page Url
     *
     * @return String
     */
    public String getTermsOfUseUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Gets Terms Of Use text
     *
     * @return String
     */
    public String getTermsOfUseText() {
        return termsText.getText();
    }
}