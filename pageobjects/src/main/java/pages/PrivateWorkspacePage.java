package main.java.pages;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateWorkspacePage extends LoadableComponent<PrivateWorkspacePage> {

    private final Logger logger = LoggerFactory.getLogger(PrivateWorkspacePage.class);

    @FindBy(css = "span.delete-button")
    private WebElement deleteButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PrivateWorkspacePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(deleteButton);
    }

    public boolean isDeleteButtonPresent() {
        return deleteButton.isDisplayed();
    }
}
