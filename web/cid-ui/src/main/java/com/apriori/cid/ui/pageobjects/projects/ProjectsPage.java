package com.apriori.cid.ui.pageobjects.projects;


import com.apriori.web.app.util.PageUtils;

import org.apache.groovy.util.Arrays;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

public class ProjectsPage extends LoadableComponent<ProjectsPage> {

    private PageUtils pageUtils;
    private WebDriver driver;

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    public ProjectsPage(WebDriver driver) {
        super();
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
    }

    public boolean isOnProjectsPage() {
        return driver.getCurrentUrl().contains("/bulk-analysis");
    }

    public boolean isListOfWorksheetsPresent() {
        List<WebElement> listOfWorksheetItems =
            pageUtils.waitForElementsToAppear(By.xpath("//div[@data-testid = 'table-body']/div"));
        return !(listOfWorksheetItems.isEmpty());
    }
}
