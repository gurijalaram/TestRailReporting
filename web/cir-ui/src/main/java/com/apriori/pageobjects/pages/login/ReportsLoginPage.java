package com.apriori.pageobjects.pages.login;

import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ReportsLoginPage extends ReportsPageHeader {

    private WebDriver driver;
    private PageUtils pageUtils;
    private AprioriLoginPage aprioriLoginPage;
    private UserCredentials userCredentials = UserUtil.getUserOnPrem();
    private boolean isEnvOnPrem = PropertiesContext.get("${env}").equals("onprem");

    @FindBy(xpath = "//input[@type='email']")
    private WebElement emailInput;

    public ReportsLoginPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginPage = new AprioriLoginPage(driver, "reports");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertThat("CIR login page was not displayed", aprioriLoginPage.getLoginTitle().contains("Cost Insight Report"));
    }

    /**
     * Login to CIR
     *
     * @return new page object
     */
    public ReportsPageHeader login() {
        return aprioriLoginPage.login(userCredentials, ReportsPageHeader.class);
    }
}