package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.ConnectionRequest;
import com.apriori.sds.entity.response.Connection;
import com.apriori.sds.entity.response.ConnectionsItemsResponse;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import com.apriori.utils.properties.PropertiesContext;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectionsTest extends SDSTestUtil {

    private static Set<String> connectionsToDelete = new HashSet<>();


    @AfterClass
    public static void clearTestingData() {
        if (!connectionsToDelete.isEmpty()) {
            connectionsToDelete.forEach(ConnectionsTest::deleteConnection);
        }

        connectionsToDelete = new HashSet<>();
    }


    @Test
    @TestRail(testCaseId = {"6936"})
    @Description("Find connections for a customer matching a specified query.")
    public void testGetConnections() {
        this.getConnections();
    }

    private List<Connection> getConnections() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_CONNECTIONS, ConnectionsItemsResponse.class);

        ResponseWrapper<ConnectionsItemsResponse> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

        return response.getResponseEntity().getItems();
    }

    @Test
    @TestRail(testCaseId = {"8625"})
    @Description("Add a Connection to the ST Installation")
    public void testPostConnections() {
        this.postConnection();
    }

    @Test
    @TestRail(testCaseId = {"8627"})
    @Description("Delete - a Connection to an Installation.")
    public void testDeleteConnections() {
            deleteConnection(this.postConnection()
                .getIdentity()
            );
    }

    @Test
    @TestRail(testCaseId = {"8626"})
    @Description("Update - a Connection to an Installation.")
    public void testPatchConnections() {
        ConnectionRequest connectionRequest = ConnectionRequest.builder()
            .deploymentIdentity("H337GKD0LA0N")
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.PATCH_CONNECTIONS_BY_ID, Connection.class)
                .inlineVariables(postConnection().getIdentity())
                .body("connection", connectionRequest);

        ResponseWrapper<Void> response = HTTP2Request.build(requestEntity).patch();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    private Connection postConnection() {
        final List<Connection> connections = this.getConnections();
        ConnectionRequest connectionRequest;

        if(!connections.isEmpty()) {
            final Connection connection  = connections.get(0);
            deleteConnection(connection.getIdentity());

            connectionRequest = this.initCustomConnectionRequest(connection);
        } else {
            connectionRequest = this.initDefaultConnectionRequest();
        }

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_CONNECTIONS, Connection.class)
                .body("connection",connectionRequest);

        ResponseWrapper<Connection> response = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, response.getStatusCode());

        connectionsToDelete.add(response.getResponseEntity().getIdentity());

        return response.getResponseEntity();
    }

    private ConnectionRequest initCustomConnectionRequest(Connection connection) {
        return ConnectionRequest.builder()
            .customerIdentity(connection.getCustomerIdentity())
            .deploymentIdentity(connection.getDeploymentIdentity())
            .installationIdentity(connection.getInstallationIdentity())
            .build();
    }

    private ConnectionRequest initDefaultConnectionRequest() {
        return ConnectionRequest.builder()
            .customerIdentity(PropertiesContext.getStr("${env}.customer_identity"))
            .deploymentIdentity("H337GKD0LA0N")
            .installationIdentity("5DF1B4HI67C6")
            .build();
    }

    private static String deleteConnection(String identityToDelete) {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.DELETE_CONNECTIONS_BY_ID, null)
                .inlineVariables(identityToDelete);

        ResponseWrapper<Void> response = HTTP2Request.build(requestEntity).delete();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, response.getStatusCode());

        connectionsToDelete.remove(identityToDelete);

        return identityToDelete;
    }
}
