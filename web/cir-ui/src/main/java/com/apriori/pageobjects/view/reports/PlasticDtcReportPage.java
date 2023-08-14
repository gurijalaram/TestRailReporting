package com.apriori.pageobjects.view.reports;

import com.apriori.PageUtils;
import com.apriori.enums.ListNameEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlasticDtcReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(PlasticDtcReportPage.class);

    @FindBy(xpath = "//span[@class='_jrHyperLink ReportExecution']/span")
    private WebElement plasticDtcDetailsRowOnePartName;

    @FindBy(xpath = "//label[@title='Outlier Distance']/input")
    private WebElement outlierDistanceElement;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public PlasticDtcReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks Distance Outlier input and scrolls down
     */
    public void clickDistanceOutlierInputAndScrollDown() {
        outlierDistanceElement.click();
        pageUtils.scrollWithJavaScript(driver.findElement(By.xpath("(//div[@title='Select Parts ']//ul)[1]")),
                true);
        waitForCorrectAvailableSelectedCount(ListNameEnum.PARTS.getListName(), "Available: ", "0");
    }

    /**
     * Gets part name from row one of Plastic Dtc Details
     *
     * @return String of row one part name
     */
    public String getPlasticDtcDetailsRowOnePartName() {
        pageUtils.waitForElementToAppear(plasticDtcDetailsRowOnePartName);
        return plasticDtcDetailsRowOnePartName.getAttribute("textContent");
    }
}
