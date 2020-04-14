package com.apriori.pageobjects.toolbars;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesignGuidanceToolbar extends LoadableComponent<DesignGuidanceToolbar> {

    private final Logger logger = LoggerFactory.getLogger(DesignGuidanceToolbar.class);

    @FindBy(css = "button[data-ap-comp='expandPanelButton']")
    private WebElement chevronButton;

    @FindBy(css = ".panel .glyphicon-remove")
    private WebElement closePanelButton;

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement helpButton;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public DesignGuidanceToolbar(WebDriver driver) {
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
        pageUtils.waitForElementAppear(closePanelButton);
        pageUtils.waitForElementAppear(chevronButton);
    }

    /**
     * Closes the design guidance
     *
     * @return current page object
     */
    public EvaluatePage closeDesignGuidance() {
        pageUtils.waitForElementAndClick(closePanelButton);
        return new EvaluatePage(driver);
    }

    /**
     * Expands the guidance panel
     *
     * @return current page object
     */
    public DesignGuidanceToolbar expandPanel() {
        pageUtils.waitForElementAndClick(chevronButton);
        return this;
    }

    /**
     * Clicks the help button
     *
     * @return the current page object
     */
    public DesignGuidanceToolbar clickHelp() {
        pageUtils.waitForElementAndClick(helpButton);
        return this;
    }

    public String getChildPageTitle() {
        return pageUtils.windowHandler().getTitle();
        // TODO remove code duplication
    }
}
