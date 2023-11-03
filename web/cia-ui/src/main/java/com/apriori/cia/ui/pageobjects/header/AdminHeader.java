package com.apriori.cia.ui.pageobjects.header;

import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class AdminHeader extends AdminPageHeader {

    private PageUtils pageUtils;

    public AdminHeader(WebDriver driver) {
        super(driver);
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
