package com.apriori.pageobjects.workflows.schedule.notifications;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FilterTab extends NotificationsPart {

    public FilterTab(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = PARENT_ELEMENT + "[id$='popup_checkbox-367']")
    private WebElement emailCheckbox;

    @FindBy(css = PARENT_ELEMENT + "[id$='popup_checkbox-368']")
    private WebElement attachReportCheckbox;

    /**
     * getter for Email Checkbox element in Notifications -> filter tab
     *
     * @return WebElement
     */
    public WebElement getEmailCheckbox() {
        return emailCheckbox;
    }

    /**
     * getter for Attach report Checkbox element in Notifications -> filter tab
     *
     * @return WebElement
     */
    public WebElement getAttachReportCheckbox() {
        return attachReportCheckbox;
    }

    /**
     * getter for first rule drop down list element
     *
     * @return WebElement
     */
    public List<String> getFilterRuleList() {
        List<String> values = new ArrayList<>();
        Select s = new Select(driver.findElement(By.name("root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-372_rule_0_filter")));
        s.getOptions().forEach(e -> values.add(e.getText()));
        return values;
    }
}
