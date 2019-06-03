package main.java.header;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public class CompareHeader extends ExploreHeader {
    private static Logger logger = LoggerFactory.getLogger(CompareHeader.class);

    @FindBy (css = "button[data-ap-comp='saveComparisonAsButton']")
    private WebElement saveAs;

}
