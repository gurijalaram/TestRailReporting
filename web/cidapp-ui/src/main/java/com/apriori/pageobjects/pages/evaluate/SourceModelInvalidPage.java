package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class SourceModelInvalidPage extends LoadableComponent<SourceModelInvalidPage> {

    @FindBy(xpath = "//button[.='Ignore']")
    private WebElement ignoreButton;

    @FindBy(css = (".modal-title"))
    private WebElement sourceModel;

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(xpath = "//button[.='Fix Source']")
    private WebElement fixSourceButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public SourceModelInvalidPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    public EvaluatePage clickIgnore() {
        pageUtils.waitForElementAndClick(ignoreButton);
        return new EvaluatePage(driver);
    }

    public String getSourceModelInvalid() {
        return pageUtils.waitForElementToAppear(sourceModel).getAttribute("textContent");
    }

    /**
     * Navigates to the explore page
     *
     * @return new page object
     */
    public ExplorePage clickExplore() {
        pageUtils.waitForElementAndClick(exploreButton);
        return new ExplorePage(driver);
    }

    public EvaluatePage clickFixSource() {
        pageUtils.waitForElementAndClick(fixSourceButton);
        return new EvaluatePage(driver);
    }
}
