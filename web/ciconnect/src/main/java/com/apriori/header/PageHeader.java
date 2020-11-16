package com.apriori.header;

import com.apriori.connectors.ConnectorList;
import com.apriori.users.UserList;
import com.apriori.utils.PageUtils;
import com.apriori.workflows.Schedule;

import cicuserguide.CicUserGuide;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */
public class PageHeader extends LoadableComponent<PageHeader> {

    private static final Logger logger = LoggerFactory.getLogger(PageHeader.class);

    @FindBy(css = "div#root_button-33 > button")
    private WebElement settingsBtn;

    @FindBy(css = "div#root_button-29 > button")
    private WebElement helpBtn;

    @FindBy(css = "div#root_button-30 > button")
    private WebElement userInfoDropdown;

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(1)")
    private WebElement workflowsMenuBtn;

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(2)")
    private WebElement usersMenuBtn;

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(3)")
    private WebElement connectorsMenuBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_label-18-bounding-box span")
    private WebElement workflowLabel;

    @FindBy(css = "div[id^='CIC_UserDropDown_MU-BMController-'][id$='_label-30'] > span")
    private WebElement currentUser;

    @FindBy(css = "div[id^='CIC_UserDropDown_MU-BMController-'][id$='_label-31'] > span")
    private WebElement currentLoginID;

    @FindBy(css = "div[id^='CIC_UserDropDown_MU-BMController-'][id$='_label-32'] > span")
    private WebElement currentCompany;

    @FindBy(css = "div[id^='CIC_HelpDropDown_MU-BMController-'][id$='_link-41'] > a > span > span > span")
    private WebElement onlineHelpLink;

    @FindBy(css = "body")
    private WebElement pageBody;

    @FindBy(id = "toolbar_logo_link")
    private WebElement pageTitle;

    @FindBy(xpath = "//span[text() = 'About Cost Insight Connect']")
    private WebElement aboutAPrioriLink;

    @FindBy(xpath = "//span[contains(text(), 'Cost Insight Connect |')]")
    private WebElement cicVersionText;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PageHeader(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * click Workflows tab
     *
     * @return new generic schedule page object from workflow tab
     */
    public Schedule clickWorkflowMenu() {
        pageUtils.waitForElementAndClick(workflowsMenuBtn);
        return new Schedule(driver);
    }

    /**
     * click Users Tab
     *
     * @return new user list page object from Users tab
     */
    public UserList clickUsersMenu() {
        pageUtils.waitForElementAndClick(usersMenuBtn);
        return new UserList(driver);
    }

    /**
     * click Connectors tab
     *
     * @return new connectors list page object from Connectors Tab
     */
    public ConnectorList clickConnectorsMenu() {
        pageUtils.waitForElementAndClick(connectorsMenuBtn);
        return new ConnectorList(driver);
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
    public PageHeader expandUserInfoDropdown() {
        pageUtils.waitForElementAndClick(userInfoDropdown);
        return new PageHeader(driver);
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
    public CicUserGuide navigateToAboutAPriori() {
        pageUtils.waitForElementAndClick(helpBtn);
        pageUtils.waitForElementAndClick(aboutAPrioriLink);
        return new CicUserGuide(driver);
    }

    /**
     * Open costing service settings modal
     *
     * @return CostingServiceSettings page object
     */
    public CostingServiceSettings openCostingServiceSettings() {
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
}