package com.apriori.pageobjects.pages.login;

import static org.junit.Assert.assertTrue;

import com.apriori.PageUtils;
import com.apriori.login.LoginService;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.reader.file.user.UserCredentials;

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
    private LoginService aprioriLoginService;

    public CidAppLoginPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.aprioriLoginService = new LoginService(driver, "cidapp");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("CID login page was not displayed", aprioriLoginService.getLoginTitle().contains("Cost Insight Design"));
    }

    /**
     * Login to CID
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public ExplorePage login(final UserCredentials userCredentials) {
        return aprioriLoginService.login(userCredentials, ExplorePage.class);
    }
}

