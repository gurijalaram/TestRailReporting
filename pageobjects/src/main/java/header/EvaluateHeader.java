package main.java.header;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public class EvaluateHeader extends LoadableComponent<EvaluateHeader> {

    private static Logger logger = LoggerFactory.getLogger(EvaluateHeader.class);

    @FindBy(css = "button[data-ap-comp='costButton']")
    private WebElement costButton;

    @FindBy(css = ".bottom .popover-content .gwt-HTML")
    private WebElement costLabelPopover;

    @FindBy(css = "li[data-ap-comp='costButton']")
    private WebElement costLabel;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement dialogCostButton;

    private final WebDriver driver;
    private final PageUtils pageUtils;
    GenericHeader genericHeader;

    public EvaluateHeader(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.genericHeader = new GenericHeader(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Selects cost button
     * @return current page object
     */
    public EvaluateHeader selectCostButton() {
        pageUtils.waitForElementToAppear(costButton).click();
        return this;
    }

    /**
     * Selects dialog cost button
     * @return current page object
     */
    public EvaluateHeader selectDialogCostButton() {
        pageUtils.waitForElementToAppear(dialogCostButton).click();
        return this;
    }

    /**
     * Wait for cost label popover
     * @return current page object
     */
    public EvaluateHeader costLabelPopover() {
        pageUtils.waitForElementToAppear(costLabelPopover);
        return this;
    }

    /**
     * Gets cost label
     * @return webelement
     */
    public WebElement getCostLabel() {
        return pageUtils.waitForElementToAppear(costLabel);
    }

    /**
     * Selects publish button
     * @return
     */
    public GenericHeader selectPublishButton() {
        return genericHeader.selectPublishButton();
    }
}