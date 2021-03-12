package com.apriori.pageobjects.pages.evaluate.components.inputs;

import com.apriori.pageobjects.common.CustomAttributesInputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomAttributesPage {

    private static final Logger logger = LoggerFactory.getLogger(CustomAttributesPage.class);

    private WebDriver driver;
    private PageUtils pageUtils;
    private CustomAttributesInputsController customAttributesInputsController;
    private ModalDialogController modalDialogController;

    public CustomAttributesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.customAttributesInputsController = new CustomAttributesInputsController(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
