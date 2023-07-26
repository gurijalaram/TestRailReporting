package enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum DMSApiEnum implements ExternalEndpointEnum {

    //
    CUSTOMER_DISCUSSIONS("%s/discussions"),
    CUSTOMER_DISCUSSION(CUSTOMER_DISCUSSIONS.getEndpointString().concat("/%s")),
    CUSTOMER_DISCUSSION_COMMENTS(CUSTOMER_DISCUSSION.getEndpointString().concat("/comments")),
    CUSTOMER_DISCUSSION_COMMENT(CUSTOMER_DISCUSSION_COMMENTS.getEndpointString().concat("/%s")),
    CUSTOMER_DISCUSSION_COMMENT_VIEWS(CUSTOMER_DISCUSSION_COMMENT.getEndpointString().concat("/views")),
    CUSTOMER_DISCUSSION_COMMENT_VIEW(CUSTOMER_DISCUSSION_COMMENT_VIEWS.getEndpointString().concat("/%s")),
    CUSTOMER_DISCUSSION_PARTICIPANTS(CUSTOMER_DISCUSSIONS.getEndpointString().concat("/%s/participants")),
    CUSTOMER_DISCUSSION_PARTICIPANT(CUSTOMER_DISCUSSIONS.getEndpointString().concat("/%s/participants/%s")),
    CUSTOMER_DISCUSSION_PROJECT_ITEM(CUSTOMER_DISCUSSIONS.getEndpointString().concat("/projectItems/%s"));

    private final String endpoint;

    DMSApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {

        return PropertiesContext.get("dms.api_url") + "customers/"
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
