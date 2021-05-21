package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteWorkflowPage {
    private static final Logger logger = LoggerFactory.getLogger(DeleteWorkflowPage.class);

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
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
    }

    /**
     * Check if the Delete popup exists
     *
     * @return True if the Delete popup exists
     */
    public boolean modalExists() {
        pageUtils.waitForElementToAppear(deleteWorklowModal);
        return deleteWorklowModal.isDisplayed();
    }

    /**
     * Get the message on the Delete poopup
     *
     * @return The message
     */
    public String getModalMessage() {
        return deleteModalMessage.getText();
    }

    /**
     * Get the header on the the Delete popup
     *
     * @return The header
     */
    public String getModalHeader() {
        return deleteModalHeader.getText();
    }

    /**
     * Delete the selected workflow
     */
    public void deleteWorkflow() {
        pageUtils.waitForElementAndClick(deleteButton);
    }

}
