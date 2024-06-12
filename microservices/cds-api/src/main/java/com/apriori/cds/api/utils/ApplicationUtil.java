package com.apriori.cds.api.utils;

import com.apriori.cds.api.enums.AppAccessControlsEnum;
import com.apriori.cds.api.enums.ApplicationEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.enums.DeploymentEnum;
import com.apriori.cds.api.models.Apps;
import com.apriori.cds.api.models.request.ApplicationInstallationRequest;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.cds.api.models.response.AccessControls;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Application;
import com.apriori.shared.util.models.response.Applications;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.LicensedApplications;
import com.apriori.shared.util.models.response.User;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplicationUtil {
//    private static RequestEntityUtil requestEntityUtil;
//    private static UserCredentials testingUser;
//
//    public ApplicationUtil() {
//        requestEntityUtil = RequestEntityUtilBuilder
//            .useRandomUser(APRIORI_DEVELOPER)
//            .useApUserContextInRequests();
//
//        testingUser = requestEntityUtil.getEmbeddedUser();
//    }

    private RequestEntityUtil requestEntityUtil;
    protected static UserCredentials testingUser;

    // constructor that accepts requestEntity (user data) we created in the test
    public ApplicationUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    // this empty constructor is needed just for now to avoid multiple errors.
    public ApplicationUtil() {
    }

    /**
     * Calls an API with GET verb
     *
     * @param applicationCloudReference - the application cloud reference
     * @return string
     */
    public String getApplicationIdentity(ApplicationEnum applicationCloudReference) {
        final RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.APPLICATIONS, Applications.class)
            .queryParams(new QueryParams().use("cloudReference[EQ]", applicationCloudReference.getApplication()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Applications> response = HTTPRequest.build(requestEntity).get();
        return response.getResponseEntity().getItems().stream().findFirst().get().getIdentity();
    }

    /**
     * this method returns the list of the application which user is entitled for
     */
    public Apps getUserApplications(User user, DeploymentEnum deploymentVar) {
        RequestEntity requestEntity =
            requestEntityUtil.init(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class)
                .inlineVariables(user.getCustomerIdentity(), user.getIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<AccessControls> accessControl = HTTPRequest.build(requestEntity).get();
        List<AccessControlResponse> accessControlItems = accessControl.getResponseEntity().getItems();

        Apps apps = Apps.builder()
            .deployment(deploymentVar.getDeployment())
            .applications(new ArrayList<>())
            .build();
        for (AccessControlResponse item : accessControlItems) {
            RequestEntity requestEntityApp =
                requestEntityUtil.init(CDSAPIEnum.APPLICATION_BY_ID, Application.class)
                    .inlineVariables(item.getApplicationIdentity())
                    .expectedResponseCode(HttpStatus.SC_OK);
            ResponseWrapper<Application> application =
                HTTPRequest.build(requestEntityApp).get();

            RequestEntity requestEntityDep =
                requestEntityUtil.init(CDSAPIEnum.DEPLOYMENT_BY_CUSTOMER_DEPLOYMENT_IDS, Deployment.class)
                    .inlineVariables(user.getCustomerIdentity(), item.getDeploymentIdentity())
                    .expectedResponseCode(HttpStatus.SC_OK);
            ResponseWrapper<Deployment> deployment =
                HTTPRequest.build(requestEntityDep).get();

            if (deployment.getResponseEntity().getName().equals(deploymentVar.getDeployment())) {
                apps.getApplications()
                    .add(AppAccessControlsEnum.fromString(application.getResponseEntity().getName()));
            }
        }
        return apps;
    }

    /**
     * @param customerIdentity     customer id
     * @param deploymentIdentity   deployment id
     * @param installationIdentity installation id
     * @param appIdentity          application id
     * @param siteIdentity         site id
     * @return new object
     */
    public ResponseWrapper<InstallationItems> addApplicationInstallation(
        String customerIdentity,
        String deploymentIdentity,
        String installationIdentity,
        String appIdentity,
        String siteIdentity) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.APPLICATION_INSTALLATION, InstallationItems.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("installation",
                ApplicationInstallationRequest.builder()
                    .applicationIdentity(appIdentity)
                    .siteIdentity(siteIdentity)
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call to add an application to a site
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site id
     * @return new object
     */
    public ResponseWrapper<LicensedApplications> addApplicationToSite(
        String customerIdentity,
        String siteIdentity,
        String appIdentity) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.APPLICATION_SITES_BY_CUSTOMER_SITE_IDS, LicensedApplications.class)
            .inlineVariables(customerIdentity, siteIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .headers(new HashMap<>() {
                {
                    put("Content-Type", "application/json");
                }
            })
            .body("licensedApplication",
                LicensedApplications.builder()
                    .applicationIdentity(appIdentity)
                    .createdBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }
}
