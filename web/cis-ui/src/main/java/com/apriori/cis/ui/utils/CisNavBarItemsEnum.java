package com.apriori.cis.ui.utils;

public enum CisNavBarItemsEnum {
    COLLABORATION("Collaboration"),
    DATA("Data"),
    ACCESS_CONTROL("Access Control"),
    DASHBOARD("Dashboard"),
    SOURCING_PROJECT("Sourcing Project"),
    PARTS_N_ASSEMBLIES("Parts & Assemblies"),
    BID_PACKAGES("Bid Packages"),
    SUPPLIERS("Suppliers"),
    USERS("Users"),
    MESSAGES("Messages");

    private final String navBarItems;

    CisNavBarItemsEnum(String navBarItems) {
        this.navBarItems = navBarItems;
    }

    public String getNavBarItems() {
        return this.navBarItems;
    }
}
