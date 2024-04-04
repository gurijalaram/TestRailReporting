package com.apriori.cis.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum CisAPIEnum implements ExternalEndpointEnum {

    //Application Metadata
    APPLICATION_METADATA("/application-metadata"),

    //BID PACKAGE
    BID_PACKAGES("/bid-packages"),
    BID_PACKAGE("/bid-packages/%s"),
    BID_PACKAGE_PROJECTS("/bid-packages/%s/projects"),
    BID_PACKAGE_PROJECT("/bid-packages/%s/projects/%s"),
    BID_PACKAGE_PROJECT_USERS("/bid-packages/%s/projects/%s/project-users"),
    BID_PACKAGE_PROJECT_USER("/bid-packages/%s/projects/%s/project-users/%s"),
    BID_PACKAGE_PROJECT_USERS_DELETE("/bid-packages/%s/projects/%s/project-users/delete"),
    BID_PACKAGE_PROJECT_ITEM_USERS("/projects/%s/project-items/%s/project-users"),
    BID_PACKAGE_PROJECT_ITEMS("/bid-packages/%s/projects/%s/project-items"),
    BID_PACKAGE_PROJECT_ITEM("/bid-packages/%s/projects/%s/project-items/%s"),
    BID_PACKAGE_ITEMS("/bid-packages/%s/bid-package-items"),
    BID_PACKAGE_ITEM("/bid-packages/%s/bid-package-items/%s"),

    // PROJECTS
    PROJECTS("/projects"),
    PROJECT("/projects/%s"),
    PROJECT_NOTIFICATIONS("/projects/notifications-count"),
    PROJECT_ITEM_NOTIFICATIONS("/projects/%s/project-items/notifications"),

    //PARTICIPANTS
    PARTICIPANTS("/participants"),

    //People
    PEOPLE("/people"),

    //User Preferences
    USERS_CURRENT_PREFERENCES("/users/current/preferences?pageSize=100"),
    //USERS_CURRENT_PREFERENCES("/users/current/preferences"),

    //Users
    USERS_CURRENT("/users/current"),

    USERS_CURRENT_EXTENDED_PREFERENCES("/users/current/extended-preferences"),

    USERS_EXTENDED_PREFERENCES("/users/extended-preferences"),

    USER_ASSIGNED_COMPONENTS("/components/already-assign-components"),

    //Discussions
    SCENARIO_DISCUSSIONS("/components/%s/scenarios/%s/scenario-discussions"),
    SCENARIO_DISCUSSION("/components/%s/scenarios/%s/scenario-discussions/%s"),
    USER_DISCUSSIONS("/user-discussions"),

    //Comments
    SCENARIO_DISCUSSION_COMMENTS("/components/%s/scenarios/%s/scenario-discussions/%s/comments"),
    SCENARIO_DISCUSSION_COMMENT("/components/%s/scenarios/%s/scenario-discussions/%s/comments/%s"),
    SCENARIO_DISCUSSION_COMMENT_BULK_MARK_AS_READ("/components/%s/scenarios/%s/scenario-discussions/%s/comments/bulk-mark-as-read");

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