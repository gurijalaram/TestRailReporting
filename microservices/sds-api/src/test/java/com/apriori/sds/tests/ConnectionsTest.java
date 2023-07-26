package com.apriori.sds.tests;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.properties.PropertiesContext;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.ConnectionRequest;
import com.apriori.sds.entity.response.Connection;
import com.apriori.sds.entity.response.ConnectionsItemsResponse;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO z: ignored due to https://jira.apriori.com/browse/BA-1943
//  shouldn't be un-ignored
@Ignore
public class ConnectionsTest extends SDSTestUtil {

    private static Set<String> connectionsToDelete = new HashSet<>();

    @Test
    @TestRail(id = {6936})
    @Description("Find connections for a customer matching a specified query.")
    @Ignore
    public void testGetConnections() {
        getConnections();
    }

    private static List<Connection> getConnections() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_CONNECTIONS, ConnectionsItemsResponse.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ConnectionsItemsResponse> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity().getItems();
    }

    @Test
    @TestRail(id = {8625})
    @Description("Add a Connection to the ST Installation")
    @Ignore
    public void testPostConnections() {
        postConnection();
    }

    @Test
    @TestRail(id = {8627})
    @Description("Delete - a Connection to an Installation.")
    @Ignore
    public void testDeleteConnections() {
        deleteConnection(postConnection()
            .getIdentity()
        );
    }

    @Test
    @TestRail(id = {8626})
    @Description("Update - a Connection to an Installation.")
    @Ignore
    public void testPatchConnections() {
        ConnectionRequest connectionRequest = ConnectionRequest.builder()
            .deploymentIdentity("H337GKD0LA0N")
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.PATCH_CONNECTIONS_BY_ID, Connection.class)
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
            RequestEntityUtil.init(SDSAPIEnum.POST_CONNECTIONS, Connection.class)
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
            RequestEntityUtil.init(SDSAPIEnum.DELETE_CONNECTIONS_BY_ID, null)
                .inlineVariables(identityToDelete)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();

        connectionsToDelete.remove(identityToDelete);

        return identityToDelete;
    }
}
