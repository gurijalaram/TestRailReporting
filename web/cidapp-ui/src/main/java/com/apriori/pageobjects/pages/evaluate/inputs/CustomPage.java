package com.apriori.pageobjects.pages.evaluate.inputs;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class CustomPage extends LoadableComponent<CustomPage> {

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Custom']")
    private WebElement customTab;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Advanced']")
    private WebElement advancedTab;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Basic']")
    private WebElement basicTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public CustomPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.inputsController = new InputsController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Custom was not selected", customTab.getAttribute("class").contains("active"));
    }

    /**
     * Opens basic tab
     *
     * @return new page object
     */
    public EvaluatePage goToBasicTab() {
        pageUtils.waitForElementAndClick(basicTab);
        return new EvaluatePage(driver);
    }

    /**
     * Opens advanced tab
     *
     * @return new page object
     */
    public AdvancedPage goToAdvancedTab() {
        pageUtils.waitForElementAndClick(advancedTab);
        return new AdvancedPage(driver);
    }
}