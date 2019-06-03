package main.java.header;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public class ExploreHeader extends GenericHeader {
    private static Logger logger = LoggerFactory.getLogger(ExploreHeader.class);

    @FindBy (css = "a.dropdown-toggle.text-center:nth-of-type(1)")
    private WebElement newBtn;

    @FindBy (css = "button[data-ap-comp='publishScenarioButton']")
    private WebElement publishBtn;

    @FindBy (css = "button[data-ap-comp='revertScenarioButton']")
    private WebElement revertBtn;

    @FindBy (css = "button[data-ap-comp='deleteScenarioButton']")
    private WebElement deleteBtn;

    @FindBy (css = "a.dropdown-toggle.text-center:nth-of-type(2)")
    private WebElement actionsBtn;
}
