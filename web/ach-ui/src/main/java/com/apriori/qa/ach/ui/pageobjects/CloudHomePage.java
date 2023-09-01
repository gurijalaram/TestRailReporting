package com.apriori.qa.ach.ui.pageobjects;

import com.apriori.PageUtils;
import com.apriori.login.UserProfilePage;
import com.apriori.properties.PropertiesContext;
import com.apriori.qa.ach.ui.dto.ApplicationDataDTO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.ArrayList;
import java.util.List;

public class CloudHomePage extends LoadableComponent<CloudHomePage> {

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "img[alt='Application Logo']")
    private WebElement logo;

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement scenarioCount;

    @FindBy(xpath = "//div[@class='user-dropdown dropdown']//button[@class='transparent dropdown-toggle btn btn-secondary']")
    private WebElement userElement;

    @FindBy(xpath = "//div[@class='deployment-connection-info']/h5[3]")
    private WebElement deploymentLabel;

    //    @FindBy(xpath = "//div[@class='dropdown-menu dropdown-menu-right show']/button[1]")
    @FindBy(xpath = "//*[local-name()='svg' and @class='svg-inline--fa fa-network-wired fa-fw']")
    private WebElement switchDeploymentButton;

    @FindBy(xpath = "//div[@class='apriori-select searchable switch-deployment-dialog-deployments css-1xzq4gn-container']")
    private WebElement deploymentSelector;


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
        pageUtils.waitForElementToAppear(logo);
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

    public List<ApplicationDataDTO> getListOfApplications() {
        List<ApplicationDataDTO> applicationsDTO = new ArrayList<>();

        List<WebElement> applications = pageUtils.waitForElementsToAppear(By.xpath("//div[contains(@class, 'application-card css')]"));

        applications.forEach(webApplication -> {
            applicationsDTO.add(ApplicationDataDTO.builder()
                .applicationName(webApplication.getAttribute("data-application"))
                .version(webApplication.getAttribute("data-version"))
                .installation(webApplication.getAttribute("data-installation"))
                .build()
            );
        });

        return applicationsDTO;
    }

    public <T> T clickWebApplicationByName(String applicationName, Class<T> webPageType) {
        By byApplicationTitle = By.xpath(String.format("//div[@data-application='%s']//div[@class='card-header']", applicationName));
        pageUtils.waitForElementAndClick(byApplicationTitle);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        driver.switchTo().window((String) driver.getWindowHandles().toArray()[1]);
        T responsePage = PageFactory.initElements(driver, webPageType);
        driver.close();
        driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);

        return responsePage;
    }


    public CloudHomePage clickUserPanel() {
        pageUtils.waitForElementAndClick(userElement);
        return this;
    }

    public SwitchDeploymentPopUpPage clickSwitchDeploymentButton() {
        pageUtils.waitForElementAndClick(switchDeploymentButton);
        return new SwitchDeploymentPopUpPage(driver);
    }

    public String getDeployment() {
        return pageUtils.waitForElementAppear(deploymentLabel).getText();
    }
}
