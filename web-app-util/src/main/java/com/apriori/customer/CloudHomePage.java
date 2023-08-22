package com.apriori.customer;

import com.apriori.PageUtils;
import com.apriori.customer.dto.ApplicationDataDTO;
import com.apriori.login.UserProfilePage;
import com.apriori.properties.PropertiesContext;

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

    @FindBy(css = "div[class='application-card css-12t9ocp']")
    private List<WebElement> applications;

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement scenarioCount;

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
        return PageFactory.initElements(driver, webPageType);
    }


}
