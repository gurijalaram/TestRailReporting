package com.apriori.cid.ui.pageobjects.bulkanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.cid.ui.pageobjects.explore.ImportCadFilePage;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class NewBulkAnalysisPage extends LoadableComponent<NewBulkAnalysisPage> {

    @FindBy(css = ".MuiPaper-root h2")
    private WebElement componentLabel;

    @FindBy(css = "button [data-icon='upload']")
    private WebElement importCadFileButton;

    @FindBy(css = "button [data-icon='circle-plus']")
    private WebElement existingScenarioButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public NewBulkAnalysisPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {
        //don't need to do anything here
    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("Import CAD File", pageUtils.waitForElementToAppear(componentLabel).getAttribute("textContent"), "Import CAD File page was not displayed");
    }

    public ImportCadFilePage importCadFiles() {
        pageUtils.waitForElementAndClick(importCadFileButton);
        return new ImportCadFilePage(driver);
    }

    public NewFromScenarioExplorePage existingScenario() {
        pageUtils.waitForElementAndClick(existingScenarioButton);
        return new NewFromScenarioExplorePage(driver);
    }

    /**
     * Closes the modal
     *
     * @return generic page object
     */
    public <T> T close(Class<T> klass) {
        return modalDialogController.close(klass);
    }
}
