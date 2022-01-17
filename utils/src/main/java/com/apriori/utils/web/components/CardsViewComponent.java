package com.apriori.utils.web.components;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.stream.Stream;

public class CardsViewComponent extends CommonComponent implements ComponentWithSpinner {

    private static final By BY_CARDS = By.cssSelector(".apriori-source-list-card-grid .user-card");

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
    public Stream<CardComponent> getCards() {
        getPageUtils().waitForCondition(this::isStable, PageUtils.DURATION_LOADING);
        return getRoot().findElements(BY_CARDS).stream()
                .map((card) -> new CardComponent(this, card));
    }
}