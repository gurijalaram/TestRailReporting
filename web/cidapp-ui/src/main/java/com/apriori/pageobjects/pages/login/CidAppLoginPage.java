package com.apriori.pageobjects.pages.login;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * @author cfrith
 */

@Slf4j
public class CidAppLoginPage extends LoadableComponent<CidAppLoginPage> {

    private WebDriver driver;
    private PageUtils pageUtils;
    private AprioriLoginPage aprioriLoginPage;

    public CidAppLoginPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.aprioriLoginPage = new AprioriLoginPage(driver, "cidapp");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("CID login page was not displayed", aprioriLoginPage.getLoginTitle(false).contains("Cost Insight Design"));
    }

    /**
     * Login to CID
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public ExplorePage login(final UserCredentials userCredentials) {
        return aprioriLoginPage.login(userCredentials, false, false, ExplorePage.class);
    }
}

