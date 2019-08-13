package main.java.pages.evaluate.process;

import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoutingsPage extends LoadableComponent<RoutingsPage> {

    private final Logger logger = LoggerFactory.getLogger(RoutingsPage.class);

    @FindBy(css = "div[data-ap-comp='routingSelectionTable'] td")
    private WebElement routingTableCells;

    @FindBy(css = "label[data-ap-field='lastCostedLabel']")
    private WebElement costedRouting;

    @FindBy(css = "label[data-ap-field='lastSelectedLabel']")
    private WebElement selectedRouting;

    @FindBy(css = "input[data-ap-field='useSelectedRouting']")
    private WebElement routingCheckBox;

    @FindBy(css = "div[data-ap-comp='routingSelectionTable'] div.v-grid-scroller-vertical")
    private WebElement routingScroller;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public RoutingsPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(routingTableCells);
    }

    /**
     * Gets the costed routing text
     * @return text as string
     */
    public String getCostedRouting() {
        return costedRouting.getText().trim();
    }

    /**
     * Gets the selected routing text
     * @return text as string
     */
    public String getSelectedRouting() {
        return selectedRouting.getText().trim();
    }

    /**
     * Selects the routing in the table
     * @param routingName - the routing
     * @return routing name as webelement
     */
    public RoutingsPage selectRouting(String routingName) {
        By routing = By.xpath("//div[@data-ap-comp='routingSelectionTable']//td[contains(text(),'" + routingName + "')]/ancestor::tr");
        pageUtils.scrollToElement(routing, routingScroller).click();
        return this;
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public ProcessPage apply() {
        applyButton.click();
        return new ProcessPage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ProcessPage cancel() {
        cancelButton.click();
        return new ProcessPage(driver);
    }
}
