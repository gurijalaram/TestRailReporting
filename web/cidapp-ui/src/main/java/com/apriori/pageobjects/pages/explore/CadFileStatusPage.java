package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * @author cfrith
 */

@Slf4j
public class CadFileStatusPage extends LoadableComponent<CadFileStatusPage> {

    @FindBy(xpath = "//button[.='Close']")
    private WebElement closeButton;

    @FindBy(css = ".modal-body .mb-3")
    private WebElement uploadStatusText;

    @FindBy(css = ".modal-body .message")
    private WebElement messageText;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public CadFileStatusPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
        //Don't really need to do anything here
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(messageText);
    }

    /**
     * Close the import modal dialog
     *
     * @return new page object
     */
    public ExplorePage close() {
        pageUtils.waitForElementAndClick(closeButton);
        return new ExplorePage(driver);
    }

    /**
     * This gets the success message after upload is completed
     *
     * @return - string
     */
    public String getImportMessage() {
        return pageUtils.waitForElementToAppear(uploadStatusText).getText();
    }
}
