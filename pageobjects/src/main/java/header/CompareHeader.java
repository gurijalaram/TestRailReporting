package main.java.header;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public class CompareHeader extends GenericHeader {

    private static Logger logger = LoggerFactory.getLogger(CompareHeader.class);

    @FindBy (css = "button[data-ap-comp='saveComparisonAsButton']")
    private WebElement saveAs;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CompareHeader(WebDriver driver) {
        super(driver);
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

    }

    public SaveAsPage saveAs() {
        pageUtils.waitForElementToAppear(saveAs).click();
        return new SaveAsPage(driver);
    }
}
