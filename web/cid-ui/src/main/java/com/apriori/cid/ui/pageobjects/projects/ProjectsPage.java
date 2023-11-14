package com.apriori.cid.ui.pageobjects.projects;


import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.LoadableComponent;

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
        return driver.getCurrentUrl().contains("/projects");
    }
}
