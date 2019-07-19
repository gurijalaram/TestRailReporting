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

public class CompareHeader extends LoadableComponent<CompareHeader> {

    private static Logger logger = LoggerFactory.getLogger(CompareHeader.class);

    @FindBy (css = "button[data-ap-comp='saveComparisonAsButton']")
    private WebElement saveAs;


    private WebDriver driver;
    private PageUtils pageUtils;
    private GenericHeader genericHeader;

    public CompareHeader(WebDriver driver) {
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

    public GenericHeader selectNewFileDropdown() {
        return genericHeader.selectNewFileDropdown();
    }


}
