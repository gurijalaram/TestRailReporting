package com.apriori.pageobjects.pages.projectsdetails;

import com.apriori.pageobjects.pages.projects.ProjectsPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

@Slf4j
public class ProjectsDetailsPage extends EagerPageComponent<ProjectsDetailsPage> {


    @FindBy(xpath = "//div[@data-testid='loader']")
    private WebElement spinner;

    @FindBy(xpath = "//h3[@data-testid='title']")
    private WebElement detailsPageTitle;

    @FindBy(xpath = "//button[@type='button']//*[@data-icon='angle-left']")
    private WebElement btnAllProjects;

    private PageUtils pageUtils;

    public ProjectsDetailsPage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;

    public ProjectsDetailsPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.waitForProjectsPageLoad();
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Method to wait projects page loads
     */
    public void waitForProjectsPageLoad() {
        getPageUtils().waitForElementToAppear(spinner);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
    }

    /**
     * Get details page title
     *
     * @return a String
     */
    public String getProjectDetailsPageTitle() {
        return getPageUtils().waitForElementAppear(detailsPageTitle).getText();
    }

    /**
     * Checks if back to all project button displayed
     *
     * @return true/false
     */
    public boolean isAllProjectsNavigationDisplayed() {
        return getPageUtils().isElementDisplayed(btnAllProjects);
    }

    /**
     * Checks if details tab lists displayed
     *
     * @return true/false
     */
    public boolean isProjectDetailsPageTabsDisplayed(String tabName) {
        return getPageUtils().isElementDisplayed(By.xpath("//div[@role='tablist']//button[contains(text(),'" + tabName + "')]"));
    }

    /**
     * clicks on all project
     *
     * @return new page object
     */
    public ProjectsPage clickOnAllProjects() {
        getPageUtils().waitForElementAndClick(btnAllProjects);
        return new ProjectsPage(driver);
    }
}