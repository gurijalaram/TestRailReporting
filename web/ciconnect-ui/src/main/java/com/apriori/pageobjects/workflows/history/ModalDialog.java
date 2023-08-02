package com.apriori.pageobjects.workflows.history;

import com.apriori.pageobjects.CICBasePage;
import com.apriori.pageobjects.workflows.WorkflowHome;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ModalDialog extends CICBasePage {
    @FindBy(css = "#confirmBox")
    private WebElement confirmAlertBox;

    @FindBy(xpath = "//div[@id='confirmButtons']/a[.='Confirm']")
    private WebElement alertBoxConfirmlBtn;

    public ModalDialog(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }


    /**
     * Clicks on Confirm button of candidates modal list
     *
     * @return WorkflowHome object
     */
    public WorkflowHome clickConfirmButton() {
        pageUtils.waitForElementAndClick(alertBoxConfirmlBtn);
        return PageFactory.initElements(driver, WorkflowHome.class);
    }
}
