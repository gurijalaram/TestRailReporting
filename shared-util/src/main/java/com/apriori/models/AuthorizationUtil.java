package com.apriori.models;

import com.apriori.enums.DeploymentsAPIEnum;
import com.apriori.enums.TokenEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.json.JsonManager;
import com.apriori.models.request.TokenRequest;
import com.apriori.models.response.ApplicationItem;
import com.apriori.models.response.Claims;
import com.apriori.models.response.DeploymentItem;
import com.apriori.models.response.GetDeploymentsResponse;
import com.apriori.models.response.InstallationItem;
import com.apriori.models.response.Token;
import com.apriori.models.response.TokenInformation;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthorizationUtil {
    private String username = PropertiesContext.get("ats.token_username");
    private String email = PropertiesContext.get("ats.token_email");
    private String issuer = PropertiesContext.get("ats.token_issuer");
    private String subject = PropertiesContext.get("${customer}.token_subject");

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
