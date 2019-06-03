package main.java.header;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public class EvaluateHeader extends ExploreHeader {
    private static Logger logger = LoggerFactory.getLogger(EvaluateHeader.class);

    @FindBy (css = "button[data-ap-comp='costButton']")
    private WebElement newBtn;

    @FindBy (css = "div[data-ap-comp='costStatus']")
    private WebElement readyToCost;
}
