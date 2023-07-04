package com.apriori.cisapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CisAPIEnum implements ExternalEndpointEnum {

    //Application Metadata
    APPLICATION_METADATA("application-metadata"),

    //BID PACKAGE
    BID_PACKAGES("bid-packages"),
    BID_PACKAGE("bid-packages/%s"),
    BID_PACKAGE_PROJECTS("bid-packages/%s/projects"),
    BID_PACKAGE_PROJECT("bid-packages/%s/projects/%s"),
    BID_PACKAGE_PROJECT_USERS("bid-packages/%s/projects/%s/project-users"),
    BID_PACKAGE_PROJECT_USER("bid-packages/%s/projects/%s/project-users/%s"),
    BID_PACKAGE_PROJECT_USERS_DELETE("bid-packages/%s/projects/%s/project-users/delete"),
    BID_PACKAGE_PROJECT_ITEM_USERS("projects/%s/project-items/%s/project-users"),
    BID_PACKAGE_PROJECT_ITEMS("bid-packages/%s/projects/%s/project-items"),
    BID_PACKAGE_PROJECT_ITEM("bid-packages/%s/projects/%s/project-items/%s"),
    BID_PACKAGE_ITEMS("bid-packages/%s/bid-package-items"),
    BID_PACKAGE_ITEM("bid-packages/%s/bid-package-items/%s"),

    // PROJECTS
    PROJECTS("projects"),
    PROJECT("projects/%s"),

    //PARTICIPANTS
    PARTICIPANTS("participants"),

    //People
    PEOPLE("people"),

    //User Preferences
    USERS_CURRENT_PREFERENCES("users/current/preferences?pageSize=100"),
    //USERS_CURRENT_PREFERENCES("users/current/preferences"),

    //Users
    USERS_CURRENT("users/current"),

    USERS_CURRENT_EXTENDED_PREFERENCES("users/current/extended-preferences"),

    USERS_EXTENDED_PREFERENCES("users/extended-preferences"),

    USER_ASSIGNED_COMPONENTS("components/already-assign-components");

    private final String endpoint;

    CisAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("cis.api_url") + String.format(getEndpointString(), variables);
    }
}