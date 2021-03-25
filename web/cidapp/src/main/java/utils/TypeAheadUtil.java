package utils;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class TypeAheadUtil {

    private static final Logger logger = LoggerFactory.getLogger(TypeAheadUtil.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    public TypeAheadUtil(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Interacts with a dropdown and input the relevant info
     *
     * @param dropdownSelector - the selector
     * @param dropdownInput    - the locator
     * @param value            - the value
     * @return current page object
     */
    public TypeAheadUtil input(WebElement dropdownSelector, WebElement dropdownInput, String value) {
        pageUtils.waitForElementAndClick(dropdownSelector);
        dropdownInput.sendKeys(value);
        dropdownInput.sendKeys(Keys.ENTER);
        return this;
    }
}
