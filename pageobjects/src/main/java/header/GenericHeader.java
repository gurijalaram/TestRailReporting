package main.java.header;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public abstract class GenericHeader extends LoadableComponent<GenericHeader> {
    protected static final Logger logger = LoggerFactory.getLogger(GenericHeader.class);

    @FindBy (css ="a[data-ap-comp='jobQueue']")
    private WebElement jobQueue;

    @FindBy (css = "a[data-ap-nav-dialog='showUnitSystemCurrency']")
    private WebElement settings;

    @FindBy (css = "a.navbar-help")
    private WebElement help;

    @FindBy (css = "a > span.glyphicon-user")
    private WebElement userInfoDropdown;

    @FindBy (css = "a[data-ap-comp='exploreButton'")
    private WebElement exploreTab;

    @FindBy (css = "button[data-ap-comp='recentScenarioButton']")
    private WebElement evaluateTab;

    @FindBy (css = "button#scenarioComparisonButton")
    private WebElement compareTab;

    private WebDriver driver;
    private PageUtils pageUtils;

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(jobQueue);
        pageUtils.waitForElementToAppear(userInfoDropdown);
    }
}
