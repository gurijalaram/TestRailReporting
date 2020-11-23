package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ComponentCostReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(ComponentCostReportPage.class);

    @FindBy(xpath = "(//ul[@class='jr-mSelectlist jr'])[2]")
    private WebElement componentSelectDropdownList;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ComponentCostReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets count of available Components in the dropdown
     * @return String
     */
    public String getComponentListCount() {
        return componentSelectDropdownList.getAttribute("childElementCount");
    }

    public int getCountOfComponentTypeElements(String componentType) {
        By locator = By.xpath(String.format(
                "(//ul[@class='jr-mSelectlist jr'])[2]/li[contains(@title, '[%s]')]", componentType));
        List<WebElement> elements = driver.findElements(locator);
        return elements.size();
    }
}
