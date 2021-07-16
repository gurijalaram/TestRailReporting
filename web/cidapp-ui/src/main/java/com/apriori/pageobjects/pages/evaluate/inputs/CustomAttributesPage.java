package com.apriori.pageobjects.pages.evaluate.inputs;

import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class CustomAttributesPage {

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public CustomAttributesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.inputsController = new InputsController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
