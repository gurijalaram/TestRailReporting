package com.apriori.cas.ui.components;

import com.apriori.shared.util.http.utils.Obligation;
import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CardComponent extends CommonComponent {
    private static final By BY_CHECKBOX_CELL = By.cssSelector(".checkbox");

    public CardComponent(CardsViewComponent owner, WebElement root) {
        super(owner.getDriver(),root);
    }

    public CheckboxComponent getCheck() {
        return Obligation.optional(() -> new CheckboxComponent(
            getDriver(),
            getPageUtils().waitForElementToAppear(BY_CHECKBOX_CELL, PageUtils.DURATION_FAST, getRoot())
        ));
    }
}