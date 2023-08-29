package com.apriori.pageobjects.home;

import com.apriori.pageobjects.CICBasePage;
import com.apriori.pageobjects.connectors.ConnectorsPage;
import com.apriori.pageobjects.home.help.CicAbout;
import com.apriori.pageobjects.home.help.CicCommunity;
import com.apriori.pageobjects.home.help.cicuserguide.CicUserGuide;
import com.apriori.pageobjects.home.settings.CostingServiceSettings;
import com.apriori.pageobjects.login.CicLoginPage;
import com.apriori.pageobjects.users.UsersPage;
import com.apriori.pageobjects.workflows.WorkflowHome;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cost Insight Connect home page
 * Navigate to workflow, users and connectors menu button
 */
public class CIConnectHome extends CICBasePage {
    private static final Logger logger = LoggerFactory.getLogger(CIConnectHome.class);

    @FindBy(xpath = "//button/span[contains(text(), 'Settings  ')]")
    private WebElement settingsBtn;

    @FindBy(xpath = "//button/span[contains(text(), 'Help')]")
    private WebElement helpBtn;

    @FindBy(css = "div#root_button-30 > button")
    private WebElement userInfoDropdown;

    @FindBy(css = "span[title='Workflows']")
    private WebElement workflowsMenuBtn;

    @FindBy(css = "span[title='Users']")
    private WebElement usersMenuBtn;

    @FindBy(css = "span[title='Connectors']")
    private WebElement connectorsMenuBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_label-18-bounding-box span")
    private WebElement workflowLabel;

    @FindBy(css = "div[id^='CIC_UserDropDown_MU-BMController-'][id$='_label-30'] > span")
    private WebElement currentUser;

    @FindBy(css = "div[id^='CIC_UserDropDown_MU-BMController-'][id$='_label-31'] > span")
    private WebElement currentLoginID;

    @FindBy(css = "div[id^='CIC_UserDropDown_MU-BMController-'][id$='_label-32'] > span")
    private WebElement currentCompany;

    @FindBy(css = "button span[class='widget-button-icon'] img[class='default'][src$='user-selected']")
    private WebElement userProfileLink;

    @FindBy(xpath = "//button/span[contains(text(), 'Logout')]")
    private WebElement logoutBtn;

    @FindBy(xpath = "//a//span[contains(text(), 'Online Help')]")
    private WebElement onlineHelpLink;

    @FindBy(xpath = "//a//span[contains(text(), 'Community')]")
    private WebElement helpCommunityLink;

    @FindBy(xpath = "//a//span[contains(text(), 'Support')]")
    private WebElement helpSupportLink;

    @FindBy(css = "body")
    private WebElement pageBody;

    @FindBy(css = "div.logo-wrapper a")
    private WebElement pageTitle;

    @FindBy(xpath = "//span[text() = 'About aP Connect']")
    private WebElement aboutAPConnectLink;

    @FindBy(xpath = "//span[contains(text(), 'Cost Insight Connect |')]")
    private WebElement cicVersionText;

    @FindBy(css = "div.tw-status-msg-box > div.status-msg-container > div.status-msg > div[id='status-msg-text']")
    private WebElement statusMessageLbl;

    public CIConnectHome(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForJavascriptLoadComplete();
    }

    /**
     * click Workflows tab
     *
     * @return new generic schedule page object from workflow tab
     */
    public WorkflowHome clickWorkflowMenu() {
        pageUtils.waitForElementAndClick(workflowsMenuBtn);
        return new WorkflowHome(driver);
    }

    /**
     * click Users Tab
     *
     * @return new user list page object from Users tab
     */
    public UsersPage clickUsersMenu() {
        pageUtils.waitForElementAndClick(usersMenuBtn);
        return new UsersPage(driver);
    }

    /**
     * click Connectors tab
     *
     * @return new connectors list page object from Connectors Tab
     */
    public ConnectorsPage clickConnectorsMenu() {
        pageUtils.waitForElementAndClick(connectorsMenuBtn);
        return new ConnectorsPage(driver);
    }


    /**
     * Get current user
     *
     * @return String
     */
    public String getCurrentUser() {
        pageUtils.waitForElementToAppear(currentUser);
        return currentUser.getText();
    }

    /**
     * Get current user Login ID
     *
     * @return String
     */
    public String getLoginID() {
        return pageUtils.waitForElementToAppear(currentLoginID).getText();
    }

    /**
     * Get current user
     *
     * @return String
     */
    public String getCurrentCompany() {
        return pageUtils.waitForElementToAppear(currentCompany).getText();
    }

    /**
     * Expand user info drop down
     *
     * @return PageHeader
     */
    public WorkflowHome expandUserInfoDropdown() {
        pageUtils.waitForElementAndClick(userInfoDropdown);
        return new WorkflowHome(driver);
    }

    /**
     * Open CIC user guide
     *
     * @return CicUserGuide
     */
    public CicUserGuide navigateToCicUserGuide() {
        pageUtils.waitForElementAndClick(helpBtn);
        pageUtils.waitForElementAndClick(onlineHelpLink);
        return new CicUserGuide(driver);
    }

    /**
     * Open CIC Help -> community link
     *
     * @return CicCommunity
     */
    public CicCommunity navigateToCicCommunity() {
        pageUtils.waitForElementAndClick(helpBtn);
        pageUtils.waitForElementAndClick(helpCommunityLink);
        return new CicCommunity(driver);
    }

    /**
     * @return CicLoginPage
     */
    public CicLoginPage clickLogout() {
        pageUtils.actionClick(userProfileLink);
        pageUtils.waitForElementAndClick(logoutBtn);
        pageUtils.waitForElementAppear(emailInputCloud);
        return new CicLoginPage(driver);
    }

    /**
     * Switches to iframe within a page by its "id" value
     *
     * @param iframeId - iframe id attribute
     * @return new CirUserGuide page object
     */
    public CicUserGuide switchToIFrameUserGuide(String iframeId) throws Exception {
        pageUtils.waitForElementToAppear(pageTitle);

        if (pageBody.getAttribute("className").startsWith("error404")) {
            throw new Exception("Link broken. Wrong page was opened - iframe wasn't found as a result");
        } else {
            driver.switchTo().frame(iframeId);
        }

        return new CicUserGuide(driver);
    }

    /**
     * Navigate to about apriori page
     *
     * @return CicUserGuide page object
     */
    public CicAbout navigateToAboutApConnect() {
        pageUtils.waitForElementAndClick(helpBtn);
        pageUtils.waitForElementAndClick(aboutAPConnectLink);
        return new CicAbout(driver);
    }

    /**
     * Open costing service settings modal
     *
     * @return CostingServiceSettings page object
     */
    public CostingServiceSettings clickCostingServiceSettings() {
        pageUtils.waitForElementAndClick(settingsBtn);
        return new CostingServiceSettings(driver);
    }

    /**
     * Get cic version text
     *
     * @return String
     */
    public String getCicVersionText() {
        return pageUtils.waitForElementToAppear(cicVersionText).getText();
    }

    /**
     * get Status Message
     *
     * @return String
     */
    public String getStatusMessage() {
        return pageUtils.waitForElementToAppear(statusMessageLbl).getText();
    }

    /**
     * get session
     *
     * @return jsession id
     */
    public String getSession() {
        pageUtils.waitForElementToBeClickable(usersMenuBtn);
        return driver.manage().getCookieNamed("JSESSIONID").getValue();
    }

    /**
     * Refresh browser
     */
    public void refreshBrowser() {
        driver.navigate().refresh();
    }
}
