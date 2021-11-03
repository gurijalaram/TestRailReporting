package com.apriori.utils.web.components;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SelectPopMenuComponent extends CommonComponent<SelectPopMenuComponent> {
    private List<WebElement> items;

    public SelectPopMenuComponent(SelectComponent parent) {
        super(parent.getDriver(), parent.getRoot());
        this.get();
    }

    public void select(String item) {
        items.stream()
            .filter((element) -> StringUtils.equals(element.getText(), item))
            .findFirst()
            .ifPresent(WebElement::click);
    }

    @Override
    protected void load() {
        items = getRoot().findElements(By.className("select-menu-item"));
    }

    @Override
    protected void isLoaded() {
        if (items == null) {
            throw new Error("The items have not been loaded.");
        }
    }
}
