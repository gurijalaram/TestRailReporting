package com.apriori.qds.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum QDSAPIEnum implements ExternalEndpointEnum {

    //BID PACKAGE
    BID_PACKAGES("bid-packages"),
    BID_PACKAGE(BID_PACKAGES.getEndpointString().concat("/%s")),
    BID_PACKAGE_PROJECTS(BID_PACKAGE.getEndpointString().concat("/projects")),
    BID_PACKAGE_PROJECT(BID_PACKAGE_PROJECTS.getEndpointString().concat("/%s")),
    BID_PACKAGE_PROJECT_USERS(BID_PACKAGE_PROJECT.getEndpointString().concat("/project-users")),
    BID_PACKAGE_PROJECT_USER(BID_PACKAGE_PROJECT_USERS.getEndpointString().concat("/%s")),
    BID_PACKAGE_PROJECT_ITEMS(BID_PACKAGE_PROJECT.getEndpointString().concat("/project-items")),
    BID_PACKAGE_PROJECT_ITEM(BID_PACKAGE_PROJECT_ITEMS.getEndpointString().concat("/%s")),
    BID_PACKAGE_ITEMS(BID_PACKAGE.getEndpointString().concat("/bid-package-items")),
    BID_PACKAGE_ITEM(BID_PACKAGE_ITEMS.getEndpointString().concat("/%s")),

    //LAYOUTS AND VIEW ELEMENTS
    LAYOUTS("layouts"),
    LAYOUT(LAYOUTS.getEndpointString().concat("/%s")),
    LAYOUT_VIEW_ELEMENTS(LAYOUT.getEndpointString().concat("/view-elements")),
    LAYOUT_VIEW_ELEMENT(LAYOUT_VIEW_ELEMENTS.getEndpointString().concat("/%s")),

    VIEW_ELEMENTS("view-elements"),
    VIEW_ELEMENT(VIEW_ELEMENTS.getEndpointString().concat("/%s")),
    VIEW_ELEMENT_LAYOUT_CONFIGURATIONS(VIEW_ELEMENT.getEndpointString().concat("/layout-configurations")),
    VIEW_ELEMENT_LAYOUT_CONFIGURATION(VIEW_ELEMENT_LAYOUT_CONFIGURATIONS.getEndpointString().concat("/%s")),
    VIEW_ELEMENT_LAYOUT_CONFIGURATION_SHARE(VIEW_ELEMENT_LAYOUT_CONFIGURATION.getEndpointString().concat("/share")),

    //PROJECTS AND PROJECT ITEMS
    PROJECTS("projects"),
    PROJECT(PROJECTS.getEndpointString().concat("/%s")),
    PROJECT_ITEMS(PROJECT.getEndpointString().concat("/project-items")),
    PROJECT_ITEM(PROJECT_ITEMS.getEndpointString().concat("/%s"));


    private final String endpoint;

    QDSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("qds.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
