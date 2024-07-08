package com.apriori.cds.api.utils;

import com.apriori.cds.api.enums.AppAccessControlsEnum;
import com.apriori.cds.api.enums.ApplicationEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.enums.DeploymentEnum;
import com.apriori.cds.api.models.DeploymentApplications;
import com.apriori.cds.api.models.request.ApplicationInstallationRequest;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.cds.api.models.response.InstallationItems;
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
    private final AccessControlUtil accessControlUtil;
    private RequestEntityUtil requestEntityUtil;

    public ApplicationUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
        this.accessControlUtil = new AccessControlUtil(requestEntityUtil);
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
     * Returns the list of the applications the user can access
     *
     * @param user          - the user object
     * @param deploymentVar - the deployment enum
     * @return new object
     */
    public DeploymentApplications getUserApplications(User user, DeploymentEnum deploymentVar) {
        List<AccessControlResponse> accessControlItems = accessControlUtil.getAccessControl(user.getCustomerIdentity(), user.getIdentity()).getItems();

        DeploymentApplications deploymentApplications = DeploymentApplications.builder()
            .deployment(deploymentVar.getDeployment())
            .applications(new ArrayList<>())
            .build();
        for (AccessControlResponse item : accessControlItems) {
            Application application = getApplication(item.getApplicationIdentity());
            Deployment deployment = getDeployment(user.getCustomerIdentity(), item.getDeploymentIdentity());

            if (deployment.getName().equals(deploymentVar.getDeployment())) {
                deploymentApplications.getApplications()
                    .add(AppAccessControlsEnum.fromString(application.getName()));
            }
        }
        return deploymentApplications;
    }

    /**
     * Calls an API with GET verb
     *
     * @param applicationIdentity - the application id
     * @return new object
     */
    public Application getApplication(String applicationIdentity) {
        RequestEntity requestEntityApp = requestEntityUtil.init(CDSAPIEnum.APPLICATION_BY_ID, Application.class)
            .inlineVariables(applicationIdentity)
            .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<Application> response = HTTPRequest.build(requestEntityApp).get();

        return response.getResponseEntity();
    }

    /**
     * Calls an API with GET verb
     *
     * @param customerIdentity   - the customer id
     * @param deploymentIdentity - the deployment id
     * @return new object
     */
    public Deployment getDeployment(String customerIdentity, String deploymentIdentity) {
        RequestEntity requestEntityDep = requestEntityUtil.init(CDSAPIEnum.DEPLOYMENT_BY_CUSTOMER_DEPLOYMENT_IDS, Deployment.class)
            .inlineVariables(customerIdentity, deploymentIdentity)
            .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<Deployment> response = HTTPRequest.build(requestEntityDep).get();

        return response.getResponseEntity();
    }

    /**
     * @param customerIdentity      - customer id
     * @param deploymentIdentity    -deployment id
     * @param installationIdentity- installation id
     * @param appIdentity           -  application id
     * @param siteIdentity          -  site id
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
