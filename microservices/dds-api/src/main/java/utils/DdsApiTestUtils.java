package utils;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import entity.request.CommentsRequest;
import entity.request.CommentsRequestParameters;
import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.response.CommentResponse;
import entity.response.DiscussionResponse;
import enums.DDSApiEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DdsApiTestUtils {

    /**
     * setup header information for DDS API Authorization
     *
     * @return Map
     */
    public static Map<String, String> setUpHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json");
        return header;
    }

    /**
     * Create Discussion
     *
     * @param discussionDesc - description
     * @param userContext    - user context
     * @return ResponseWrapper of DiscussionResponse object
     */
    public static ResponseWrapper<DiscussionResponse> createDiscussion(String discussionDesc, String userContext) {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ACTIVE")
                .description(discussionDesc)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSIONS, DiscussionResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"))
            .headers(setUpHeader())
            .body(discussionsRequest)
            .apUserContext(userContext);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Delete Discussion
     *
     * @param discussionIdentity - Discussion Identity
     * @param userContext        - user context
     * @return ResponseWrapper of String
     */
    public static ResponseWrapper<String> deleteDiscussion(String discussionIdentity, String userContext) {
        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION, null)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(setUpHeader())
            .apUserContext(userContext);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Create Discussion comment
     *
     * @param commentDesc        - description
     * @param users              - list of users
     * @param discussionIdentity - discussion identity
     * @param userContext        - user context
     * @return - ResponseWrapper of CommentResponse object
     */
    public static ResponseWrapper<CommentResponse> createComment(String commentDesc, List<String> users, String discussionIdentity, String userContext) {
        CommentsRequest commentsRequest = CommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("ACTIVE")
                .content(commentDesc)
                .mentionedUserEmails((ArrayList<String>) users).build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENTS, CommentResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .body(commentsRequest)
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Delete discussion Comment
     *
     * @param discussionIdentity - discussion identity
     * @param userContext        - user context
     * @return - ResponseWrapper of string object
     */
    public static ResponseWrapper<String> deleteComment(String discussionIdentity, String commentIdentity, String userContext) {
        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENT, null)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity)
            .headers(setUpHeader())
            .apUserContext(userContext);

        return HTTPRequest.build(requestEntity).delete();
    }
}
