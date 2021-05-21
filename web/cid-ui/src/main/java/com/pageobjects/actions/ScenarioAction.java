package com.pageobjects.actions;

import com.apriori.apibase.services.response.objects.SubmitWorkOrder;
import com.apriori.apibase.services.response.objects.WorkOrderCommand;
import com.apriori.apibase.services.response.objects.WorkOrderInputs;
import com.apriori.apibase.services.response.objects.WorkOrderRequestEntity;
import com.apriori.apibase.services.response.objects.WorkOrderScenarioIteration;
import com.apriori.apibase.services.response.objects.WorkOrdersWrapper;
import com.apriori.apibase.services.response.objects.WorkSpaceSearchCriteria;
import com.apriori.apibase.services.response.objects.WorkSpaceSearchWrapper;
import com.apriori.utils.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.utils.http.builder.service.HTTPRequest;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;

public class ScenarioAction {

    private HashMap<String, String> authorizationHeader;
    private String baseURL;

    /**
     * Default delete scenario by credentials from UserEnum and scenario CONTAINING name
     * Steps to delete:
     * find scenario by contain scenario name
     * submit scenario to delete
     * execute submitted delete scenario command
     *
     * @param workOrderRequestEntity
     */
    public static void forceDelete(WorkOrderRequestEntity workOrderRequestEntity) {
        new ScenarioAction().doApiDelete(workOrderRequestEntity);
    }

    private void doApiDelete(WorkOrderRequestEntity workOrderRequestEntity) {

        this.baseURL = workOrderRequestEntity.getBaseURL();
        this.authorizationHeader = this.initAuthorizationHeader(workOrderRequestEntity);

        WorkOrdersWrapper workOrdersWrapper = this.findWorkOrders(workOrderRequestEntity);

        if (listFilteredOrdersIsEmpty(workOrdersWrapper)) {
            throw new IllegalArgumentException("Wrong search criteria for scenario");
        }

        workOrdersWrapper.getScenarioInfos()
            .forEach(scenarioInfo -> this.doDeleteScenario(scenarioInfo.getWorkOrderScenarioIteration()));
    }

    private WorkOrdersWrapper findWorkOrders(WorkOrderRequestEntity workOrderRequestEntity) {
        return workOrderRequestEntity.getWorkSpaceSearchWrapper() != null ? this.getOrderByFilter(authorizationHeader, workOrderRequestEntity.getWorkSpaceSearchWrapper())
            : this.getOrderByFilter(authorizationHeader, this.initDefaultSearchCriteria(workOrderRequestEntity));
    }

    private boolean listFilteredOrdersIsEmpty(WorkOrdersWrapper workOrdersWrapper) {
        return workOrdersWrapper.getTotalAvailableCount() < 1;
    }

    private void doDeleteScenario(WorkOrderScenarioIteration workOrderScenarioIteration) {

        SubmitWorkOrder submitWorkOredr = this.doSubmitWorkOrder(workOrderScenarioIteration);

        new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(authorizationHeader)
            .setReturnType(SubmitWorkOrder.class)
            .setEndpoint(baseURL + "ws/workorder/orderstatus")
            .setBody(new SubmitWorkOrder()
                .setOrderIds(Collections.singletonList(submitWorkOredr.getId()))
                .setAction("SUBMIT")
            )
            .setStatusCode(200)
            .commitChanges()
            .connect()
            .post();

    }

    private SubmitWorkOrder doSubmitWorkOrder(WorkOrderScenarioIteration workOrderScenarioIteration) {
        WorkOrderCommand workOrderCommand = new WorkOrderCommand()
            .setCommandType("DELETE")
            .setInputs(new WorkOrderInputs()
                .setIncludeOtherWorkspace(false)
                .setWorkOrderScenarioIteration(workOrderScenarioIteration)
            );

        ResponseWrapper<SubmitWorkOrder> submitWorkOredrResponseWrapper = new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(authorizationHeader)
            .setReturnType(SubmitWorkOrder.class)
            .setEndpoint(baseURL + "ws/workorder/orders")
            .setBody(workOrderCommand)
            .setStatusCode(201)
            .commitChanges()
            .connect()
            .post();

        return submitWorkOredrResponseWrapper.getResponseEntity();
    }

    private WorkOrdersWrapper getOrderByFilter(final HashMap<String, String> authorizationHeader, WorkSpaceSearchWrapper workSpaceSearch) {
        new HTTPRequest().unauthorized()
            .customizeRequest()
            .setEndpoint(baseURL + "ws/workspace/users/me/saved-searches?name=default")
            .setHeaders(authorizationHeader)
            .setStatusCode(HttpStatus.SC_OK)
            .setBody(workSpaceSearch)
            .commitChanges()
            .connect()
            .post();

        return (WorkOrdersWrapper) new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(authorizationHeader)
            .setStatusCode(HttpStatus.SC_OK)
            .setReturnType(WorkOrdersWrapper.class)
            .setEndpoint(baseURL + "ws/scenario-search/search/savedSearch?name=default&limit=1000")
            .commitChanges()
            .connect()
            .get().getResponseEntity();
    }


    private WorkSpaceSearchWrapper initDefaultSearchCriteria(final WorkOrderRequestEntity workOrderRequestEntity) {

        return new WorkSpaceSearchWrapper()
            .setVisibility(workOrderRequestEntity.getWorkspace())
            .setCriteria(new WorkSpaceSearchCriteria()
                .setWorkspaceIDs(Collections.singletonList(0))
                .setScenarioName(workOrderRequestEntity.getScenarioName())
                .setScenarioTypes(Collections.singletonList(workOrderRequestEntity.getScenarioType().getType()))
                .setIncludeAllStateIterations(workOrderRequestEntity.isIncludeAllStateIterations()));
    }

    private HashMap<String, String> initAuthorizationHeader(WorkOrderRequestEntity workOrderRequestEntity) {

        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + authenticateUser(workOrderRequestEntity.getUsername(),
                    workOrderRequestEntity.getPassword())
                );
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
            }};
    }

    private String authenticateUser(String username, String password) {
        ResponseWrapper<AuthenticateJSON> authenticateJSONResponseWrapper = new HTTPRequest().defaultFormAuthorization(username, password)
            .customizeRequest()
            .setReturnType(AuthenticateJSON.class)
            .setEndpoint(baseURL + "ws/auth/token")
            .setStatusCode(HttpStatus.SC_OK)
            .setAutoLogin(false)
            .commitChanges()
            .connect()
            .post();

        return authenticateJSONResponseWrapper.getResponseEntity().getAccessToken();
    }
}
