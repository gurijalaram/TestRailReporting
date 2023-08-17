package com.apriori.login;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.EagerPageComponent;
import com.apriori.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author cfrith
 */

@Slf4j
public class PrivacyPolicyPage extends EagerPageComponent<PrivacyPolicyPage> {

    @FindBy(id = "menu-main-menu")
    private WebElement mainMenu;

    @FindBy(css = ".privacy-page h1")
    private WebElement aprioriHeading;

    @FindBy(css = "a img[alt='aPriori']")
    private WebElement aprioriLogo;

    private PageUtils pageUtils;

    public PrivacyPolicyPage(WebDriver driver) {
        super(driver, log);
        log.debug(getPageUtils().currentlyOnPage(this.getClass().getSimpleName()));
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(isPageLogoDisplayed(), "aPriori logo is not displayed");
    }

    /**
     * Gets page heading
     *
     * @return - string
     */
    public String getPageHeading() {
        getPageUtils().windowHandler(1);
        return aprioriHeading.getText();
    }

    /**
     * Gets page logo
     *
     * @return - webelement
     */
    public boolean isPageLogoDisplayed() {
        getPageUtils().windowHandler(1);
        return getPageUtils().waitForElementToAppear(aprioriLogo).isDisplayed();
    }

    /**
     * Gets window url
     *
     * @return - string
     */
    public String getChildWindowURL() {
        return getPageUtils().getTabTwoUrl();
    }

    /**
     * Gets count of open tabs
     *
     * @return int - open tab count
     */
    public int getTabCount() {
        return getPageUtils().getCountOfOpenTabs();
    }
}
