package com.apriori.components;

import com.apriori.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.stream.Stream;

public class CardsViewComponent extends CommonComponent implements ComponentWithSpinner {

    public CardsViewComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    @Override
    public boolean isStable() {
        return getPageUtils().findLoader(getRoot()) == null;
    }

    /**
     * Gets the cards stream.
     *
     * @return The cards stream.
     */
    public Stream<CardComponent> getCards(String cardType) {
        getPageUtils().waitForCondition(this::isStable, PageUtils.DURATION_LOADING);
        return getRoot().findElements(By.cssSelector(String.format(".apriori-source-list-card-grid .%s", cardType))).stream()
                .map((card) -> new CardComponent(this, card));
    }
}