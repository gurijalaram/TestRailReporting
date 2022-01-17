package com.apriori.utils.web.components;

import org.openqa.selenium.WebElement;

public class CardComponent extends CommonComponent {

    public CardComponent(CardsViewComponent owner, WebElement root) {
        super(owner.getDriver(),root);
    }
}