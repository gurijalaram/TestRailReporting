package com.apriori.pageobjects.home.help;

import com.apriori.pageobjects.CICBasePage;
import com.apriori.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class CicCommunity extends CICBasePage {

    @FindBy(xpath = "//a/b[text() = 'Submit a Request']")
    private WebElement submitRequestLink;

    public CicCommunity(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    protected void load() {
    }

    protected void isLoaded() throws Error {

    }

    /**
     * Switch to second tab
     *
     * @return CicUserGuide Page
     */
    public CicCommunity switchTab() {
        pageUtils.windowHandler(1);
        return new CicCommunity(driver);
    }

    /**
     * Get tab two URL
     *
     * @return String
     */
    public String getURL() {
        return pageUtils.getTabTwoUrl();
    }

}
