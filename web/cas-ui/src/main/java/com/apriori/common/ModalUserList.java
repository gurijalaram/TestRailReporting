package com.apriori.common;

import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.SourceListComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ModalUserList {

    @FindBy(css = ".MuiPaper-root[role='dialog']")
    private WebElement candidatesSourceListRoot;

    @FindBy(css = "[data-testid='primary-button']")
    private WebElement candidatesAddButton;

    @FindBy(css = "[data-testid='secondary-button']")
    private WebElement candidatesCancelButton;

    @FindBy(css = ".close-modal")
    private WebElement candidatesCloseButton;

    @FindBy(xpath = "//button[.='Ok']")
    private WebElement candidatesConfirmOkButton;

    @FindBy(css = ".candidate-confirm-modal .MuiDialogActions-spacing [data-testid='secondary-button']")
    private WebElement candidatesConfirmCancelButton;

    @FindBy(css = ".candidate-confirm-modal .fa-circle-xmark")
    private WebElement candidatesConfirmCloseButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ModalUserList(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets the underlying user candidates source list.
     *
     * @return The user candidates source list.
     */
    public SourceListComponent getCandidates() {
        WebElement root = pageUtils.waitForElementToAppear(candidatesSourceListRoot);
        SourceListComponent candidatesSourceList = new SourceListComponent(driver, root);
        pageUtils.waitForCondition(candidatesSourceList::isStable, PageUtils.DURATION_LOADING);
        return candidatesSourceList;
    }

    /**
     * Clicks on Cancel button of candidates modal list
     *
     * @return this object
     */
    public <T> T clickCandidatesCancelButton(Class<T> klass) {
        pageUtils.waitForElementAndClick(candidatesCancelButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Clicks on Close button of candidates modal list
     *
     * @return this object
     */
    public <T> T clickCandidatesCloseButton(Class<T> klass) {
        pageUtils.waitForElementAndClick(candidatesCloseButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Clicks on Add button of candidates modal list
     *
     * @return this object
     */
    public <T> T clickCandidatesAddButton(Class<T> klass) {
        pageUtils.waitForElementAndClick(candidatesAddButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Clicks on Cancel button of candidates confirm dialog
     *
     * @return this object
     */
    public <T> T clickCandidatesConfirmCancelButton(Class<T> klass) {
        pageUtils.waitForElementAndClick(candidatesConfirmCancelButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Clicks on Ok button of candidates confirm dialog
     *
     * @return this object
     */
    public <T> T clickCandidatesConfirmOkButton(Class<T> klass) {
        pageUtils.waitForElementAndClick(candidatesConfirmOkButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Clicks on Close button of candidates confirm dialog
     *
     * @return this object
     */
    public <T> T clickCandidatesConfirmCloseButton(Class<T> klass) {
        pageUtils.waitForElementAndClick(candidatesConfirmCloseButton);
        return PageFactory.initElements(driver, klass);
    }
}
