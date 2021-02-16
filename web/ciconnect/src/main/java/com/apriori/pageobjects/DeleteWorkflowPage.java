package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteWorkflowPage {
    private final Logger logger = LoggerFactory.getLogger(DeleteWorkflowPage.class);

    @FindBy(css = "#confirmBox")
    private WebElement deleteWorklowModal;
    @FindBy(css = "#confirmBox > div.confirmHeader.modal-header > ul > li.title")
    private WebElement deleteModalHeader;
    @FindBy(css = "#confirmBox > div.confirmBody.modal-body")
    private WebElement deleteModalMessage;
    @FindBy(css = "#confirmButtons > a.button.btn.blue")
    private WebElement deleteButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public DeleteWorkflowPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.pageUtils = PageUtils.getInstance(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
    }

    public boolean modalExists() {
        pageUtils.waitForElementToAppear(deleteWorklowModal);
        return deleteWorklowModal.isDisplayed();
    }

    public String getModalMessage() {
        return deleteModalMessage.getText();
    }

    public String getModalHeader() {
        return deleteModalHeader.getText();
    }

    public void deleteWorkflow() {
        pageUtils.waitForElementAndClick(deleteButton);
    }

}