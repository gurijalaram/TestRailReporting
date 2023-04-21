package com.apriori.pageobjects.pages.projects;

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
public class ProjectsPage extends EagerPageComponent<ProjectsPage> {

    @FindBy(xpath = "//h3[@data-testid='title']")
    private WebElement title;

    @FindBy(id = "create-new-button")
    private WebElement btnCreateNewProject;

    @FindBy(xpath = "//div[@data-testid='loader']")
    private WebElement spinner;

    private PageUtils pageUtils;

    public ProjectsPage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;

    public ProjectsPage(WebDriver driver, Logger logger) {
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
     * Get the page title
     *
     * @return a string
     */
    public String getPageTitle() {
        return getPageUtils().waitForElementAppear(title).getText();
    }

    /**
     * Checks if create new project button displayed
     *
     * @return true/false
     */
    public boolean isCreateNewProjectsOptionDisplayed() {
        return getPageUtils().isElementDisplayed(btnCreateNewProject);
    }
}