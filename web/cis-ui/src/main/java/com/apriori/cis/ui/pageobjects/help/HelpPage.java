package com.apriori.cis.ui.pageobjects.help;

import com.apriori.web.app.util.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class HelpPage extends EagerPageComponent<HelpPage> {

    @FindBy(xpath = "//button[.='Support']")
    private WebElement supportElement;

    @FindBy(xpath = "//button[.='About aPriori']")
    private WebElement aboutApriori;

    public HelpPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(aboutApriori);
    }

    /**
     * Clicks the Support button
     *
     * @return new page object
     */
    public ZendeskSignInPage clickSupport() {
        getPageUtils().waitForElementAndClick(supportElement);
        return new ZendeskSignInPage(getDriver());
    }

    /**
     * Clicks on the About aPriori button
     *
     * @return new page object
     */
    public AboutAprioriPage clickAboutApriori() {
        getPageUtils().waitForElementAndClick(aboutApriori);
        return new AboutAprioriPage(getDriver());
    }
}
