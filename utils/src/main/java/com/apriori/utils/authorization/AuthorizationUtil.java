package com.apriori.utils.authorization;

import com.apriori.utils.ApplicationItem;
import com.apriori.utils.DeploymentItem;
import com.apriori.utils.GetDeploymentsResponse;
import com.apriori.utils.InstallationItem;
import com.apriori.utils.enums.DeploymentsAPIEnum;
import com.apriori.utils.enums.TokenEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthorizationUtil {
    private String username = PropertiesContext.get("ats.token_username");
    private String email = PropertiesContext.get("ats.token_email");
    private String issuer = PropertiesContext.get("ats.token_issuer");
    private static final ObjectMapper objectMapper = new ObjectMapper();
    //    private String subject = PropertiesContext.get("ap-int.common.token_subject");
    private String subject = PropertiesContext.get("${customer}.${${customer}.environment_type}.token_subject");
    public AuthorizationUtil(UserCredentials userCredentials) {
        this.username = userCredentials.getUsername();
        this.email = userCredentials.getEmail();
    }

    public AuthorizationUtil() {
    }

    /**
     * POST to get a JWT token
     *
     * @return string
     */
    public ResponseWrapper<Token> getToken() {
        log.info("Getting ATS Token...");

        RequestEntity requestEntity = RequestEntityUtil.init(TokenEnum.POST_TOKEN, Token.class)
            .body(TokenRequest.builder()
                .token(TokenInformation.builder()
                    .issuer(issuer)
                    .subject(subject)
                    .claims(Claims.builder()
                        .name(username)
                        .email(email)
                        .build())
                    .build())
                .build())
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Gets token as string
     *
     * @return string
     */
    public String getTokenAsString() {
        return getToken().getResponseEntity().getToken();
    }

    /**
     * Gets deployments
     *
     * @param userCredentials - UserCredentials instance containing user details to use in api call
     * @param queryParams     - Map of key value pairs to add to url
     * @return List of Deployment Items
     */
    private List<DeploymentItem> getDeploymentItems(UserCredentials userCredentials, QueryParams queryParams) {
        final RequestEntity requestEntity = RequestEntityUtil
            .init(DeploymentsAPIEnum.DEPLOYMENTS, null)
            .token(userCredentials.getToken())
            .inlineVariables(
                PropertiesContext.get("customer_identity"),
                PropertiesContext.get("secret_key")
            )
            .queryParams(queryParams)
            .expectedResponseCode(HttpStatus.SC_OK);

        return JsonManager.convertBodyToJson(HTTPRequest.build(requestEntity).get(), GetDeploymentsResponse.class).getItems();
    }

    /**
     * Gets deployments response object to allow for usage of response
     *
     * @param userCredentials UserCredentials instance containing user details to use in api call
     * @return GetDeploymentsResponse instance
     */
    private DeploymentItem getDeploymentByName(UserCredentials userCredentials, String deploymentName) {
        QueryParams filterMap = new QueryParams();
        filterMap.put("name[EQ]", deploymentName);
        List<DeploymentItem> deploymentItems = getDeploymentItems(userCredentials, filterMap);
        return deploymentItems.stream().findFirst().orElseThrow(() -> new RuntimeException("Deployment not found"));
    }

    /**
     * Gets Authorisation Target Cloud Context
     *
     * @param userCredentials - user credentials
     * @return String - cloud context
     */
    public String getAuthTargetCloudContext(UserCredentials userCredentials) {
        String cloudContext = PropertiesContext.get("customer_identity");

        String applicationNameFromConfig = PropertiesContext.get("application_name");

        DeploymentItem deploymentItem = getDeploymentByName(userCredentials, PropertiesContext.get("deployment"));

        InstallationItem installationItem = deploymentItem.getInstallations()
            .stream()
            .filter(element -> element.getName().equals(PropertiesContext.get("installation_name")))
            .limit(1)
            .collect(Collectors.toList()).get(0);

        ApplicationItem applicationItem = installationItem.getApplications()
            .stream()
            .filter(element -> element.getServiceName().equalsIgnoreCase(applicationNameFromConfig))
            .limit(1)
            .collect(Collectors.toList()).get(0);

        return cloudContext.concat(deploymentItem.getIdentity()).concat(installationItem.getIdentity()).concat(applicationItem.getIdentity());
    }
}
