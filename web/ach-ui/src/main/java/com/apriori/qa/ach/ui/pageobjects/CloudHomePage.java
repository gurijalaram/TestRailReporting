package com.apriori.qa.ach.ui.pageobjects;

import com.apriori.ach.api.dto.ApplicationDTO;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.web.app.util.PageUtils;
import com.apriori.web.app.util.login.UserProfilePage;

import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CloudHomePage extends LoadableComponent<CloudHomePage> {

    @FindBy(css = "img[alt='Application Logo']")
    private WebElement logo;

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement scenarioCount;

    @FindBy(xpath = "//button[@class='transparent dropdown-toggle btn btn-secondary']")
    private WebElement userElement;

    @FindBy(xpath = "//div[@class='deployment-connection-info']/h5[3]")
    private WebElement deploymentLabel;

    @FindBy(xpath = "//*[local-name()='svg' and @class='svg-inline--fa fa-network-wired fa-fw']")
    private WebElement switchDeploymentButton;
    @FindBy(xpath = "//*[local-name()='svg' and @data-icon='users']")
    private WebElement userManagementButton;

    @FindBy(xpath = "//div[@class='apriori-select searchable switch-deployment-dialog-deployments css-1xzq4gn-container']")
    private WebElement deploymentSelector;

    private StringBuilder loadApplicationsErrors = new StringBuilder();

    private PageUtils pageUtils;
    private WebDriver driver;

    private String userTokenFromBrowser;

    public CloudHomePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToBeClickable(logo);
        pageUtils.waitForElementToBeClickable(userElement);
    }

    /**
     * Opens profile user page
     *
     * @return UserProfilePage object
     */
    public UserProfilePage goToProfilePage() {
        driver.get(PropertiesContext.get("cloud.ui_url").concat("user-profile"));
        return new UserProfilePage(driver);
    }

    /**
     * Get list of applications from user screen and map them into ApplicationDTO objects
     * @return
     */
    public List<ApplicationDTO> getListOfApplications() {
        List<ApplicationDTO> applicationsDTO = new ArrayList<>();

        List<WebElement> applications = pageUtils.waitForElementsToAppear(By.xpath("//div[contains(@class, 'application-card css')]"));

        applications.forEach(webApplication -> {
            applicationsDTO.add(ApplicationDTO.builder()
                .applicationName(webApplication.getAttribute("data-application"))
                .version(webApplication.getAttribute("data-version"))
                .installation(webApplication.getAttribute("data-installation"))
                .build()
            );
        });

        return applicationsDTO;
    }

    /**
     * Click on web application on CloudHomePage by application name in UI
     * @param applicationName
     * @param webPageType
     * @return
     * @param <T>
     */
    public <T> T clickWebApplicationByName(String applicationName, Class<T> webPageType) {
        By byApplicationTitle = By.xpath(String.format("//div[@data-application='%s']//div[@class='card-header']", applicationName));
        By byLoadingTitle = By.xpath("//div[@class='loader large-loader opaque full-screen']");
        pageUtils.waitForElementAndClick(byApplicationTitle);

        pageUtils.waitForElementToAppear(byLoadingTitle);
        pageUtils.waitForElementsToNotAppear(byLoadingTitle);

        driver.switchTo().window((String) driver.getWindowHandles().toArray()[1]);
        return PageFactory.initElements(driver, webPageType);
    }

    /**
     * Do click on web application wait until application page is loaded and close application tab
     * @param applicationName
     * @param webPageType
     * @return
     */

    public <T> void clickWebApplicationByNameAndCloseAfterLoad(String applicationName, Class<T> webPageType) {
        try {
            T responsePage  = clickWebApplicationByName(applicationName, webPageType);
        } catch (Exception e) {

            captureApplicationScreenshot(applicationName);
            final String errorText = String.format("Failed to load application with the name %s and class type %s \n " +
                "Error message: %s", applicationName, webPageType, e.getMessage());

            log.info(errorText, e);
            loadApplicationsErrors.append(errorText)
                .append("\n");
        }

        pageUtils.waitFor(10000); // workaround for OAUTH issue
        driver.close();
        driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);
    }

    @SneakyThrows
    private void captureApplicationScreenshot(String applicationName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String filename = File.separator
            + "target"
            + File.separator
            + "screenshots"
            + File.separator
            + "screenshot-application-"
            + applicationName + "-"
            + this.getClass().getName() + "-" + "chrome" + 1 + ".png";

        FileUtils.copyFile(screenshot, new File(filename));
        Allure.addAttachment(filename, FileUtils.openInputStream(screenshot));
    }

    /**
     * Click userPanel
     * @return
     */
    public CloudHomePage clickUserPanel() {
        pageUtils.waitForElementAndClick(userElement);
        return this;
    }

    /**
     * Click switch deployment button
     * @return
     */
    public SwitchDeploymentPopUpPage clickSwitchDeploymentButton() {
        pageUtils.waitForElementAndClick(switchDeploymentButton);
        return new SwitchDeploymentPopUpPage(driver);
    }

    /**
     * go to the user management page
     * @return new user management page
     */
    public UserManagementPage clickUserManagementButton() {
        pageUtils.waitForElementAndClick(userManagementButton);
        return new UserManagementPage(driver);
    }

    public String getDeployment() {
        return pageUtils.waitForElementAppear(deploymentLabel).getText();
    }

    /**
     * Get user ID_TOKEN from browser local storage
     * @return
     */
    public String getUserTokenFromBrowser() {
        if (StringUtils.isBlank(userTokenFromBrowser)) {
            userTokenFromBrowser = pageUtils.getItemFromLocalStorage("ID_TOKEN");
        }

        return userTokenFromBrowser;
    }

    /**
     * Get string of load errors, if there are present
     * @return
     */
    public String getLoadApplicationsErrors() {
        return loadApplicationsErrors.toString();
    }

    public boolean isSwitchDeploymentButtonExist() {
        this.clickUserPanel();
        final boolean isSwitchButtonExist = pageUtils.isElementDisplayed(switchDeploymentButton);
        this.clickUserPanel();

        return isSwitchButtonExist;
    }
}
