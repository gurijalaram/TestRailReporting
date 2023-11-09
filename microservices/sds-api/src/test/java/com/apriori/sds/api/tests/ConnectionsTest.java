package com.apriori.sds.api.tests;

import com.apriori.sds.api.enums.SDSAPIEnum;
import com.apriori.sds.api.models.request.ConnectionRequest;
import com.apriori.sds.api.models.response.Connection;
import com.apriori.sds.api.models.response.ConnectionsItemsResponse;
import com.apriori.sds.api.util.SDSTestUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO : ignored due to https://jira.apriori.com/browse/BA-1943
//  shouldn't be un-ignored
@Disabled
@ExtendWith(TestRulesAPI.class)
public class ConnectionsTest extends SDSTestUtil {

    private static Set<String> connectionsToDelete = new HashSet<>();

    @Test
    @TestRail(id = {6936})
    @Description("Find connections for a customer matching a specified query.")
    @Disabled
    public void testGetConnections() {
        getConnections();
    }

    private static List<Connection> getConnections() {
        final RequestEntity requestEntity =
            RequestEntityUtil_Old.init(SDSAPIEnum.GET_CONNECTIONS, ConnectionsItemsResponse.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ConnectionsItemsResponse> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity().getItems();
    }

    @Test
    @TestRail(id = {8625})
    @Description("Add a Connection to the ST Installation")
    @Disabled
    public void testPostConnections() {
        postConnection();
    }

    @Test
    @TestRail(id = {8627})
    @Description("Delete - a Connection to an Installation.")
    @Disabled
    public void testDeleteConnections() {
        deleteConnection(postConnection()
            .getIdentity()
        );
    }

    @Test
    @TestRail(id = {8626})
    @Description("Update - a Connection to an Installation.")
    @Disabled
    public void testPatchConnections() {
        ConnectionRequest connectionRequest = ConnectionRequest.builder()
            .deploymentIdentity("H337GKD0LA0N")
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil_Old.init(SDSAPIEnum.PATCH_CONNECTIONS_BY_ID, Connection.class)
                .inlineVariables(postConnection().getIdentity())
                .body("connection", connectionRequest)
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).patch();
    }

    private static Connection postConnection() {
        final List<Connection> connections = getConnections();
        ConnectionRequest connectionRequest;

        if (!connections.isEmpty()) {
            final Connection connection = connections.get(0);
            deleteConnection(connection.getIdentity());

            connectionRequest = initCustomConnectionRequest(connection);
        } else {
            connectionRequest = initDefaultConnectionRequest();
        }

        final RequestEntity requestEntity =
            RequestEntityUtil_Old.init(SDSAPIEnum.POST_CONNECTIONS, Connection.class)
                .body("connection", connectionRequest)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<Connection> response = HTTPRequest.build(requestEntity).post();

        connectionsToDelete.add(response.getResponseEntity().getIdentity());

        return response.getResponseEntity();
    }

    private static ConnectionRequest initCustomConnectionRequest(Connection connection) {
        return ConnectionRequest.builder()
            .customerIdentity(connection.getCustomerIdentity())
            .deploymentIdentity(connection.getDeploymentIdentity())
            .installationIdentity(connection.getInstallationIdentity())
            .build();
    }

    private static ConnectionRequest initDefaultConnectionRequest() {
        return ConnectionRequest.builder()
            .customerIdentity(PropertiesContext.get("customer_identity"))
            .deploymentIdentity("H337GKD0LA0N")
            .installationIdentity("5DF1B4HI67C6")
            .build();
    }

    private static String deleteConnection(String identityToDelete) {
        final RequestEntity requestEntity =
            RequestEntityUtil_Old.init(SDSAPIEnum.DELETE_CONNECTIONS_BY_ID, null)
                .inlineVariables(identityToDelete)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();

        connectionsToDelete.remove(identityToDelete);

        return identityToDelete;
    }
}
