package com.apriori.apibase.utils;

import com.apriori.apibase.services.response.objects.ColumnEntity;
import com.apriori.apibase.services.response.objects.DisplayColumnsEntity;
import com.apriori.utils.http.builder.service.HTTPRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility showing adding columns to CI Design Explore view using API
 */
public class UISetup {
// TODO z:
//    private APIAuthentication apiAuthentication = new APIAuthentication();
//    private DisplayColumnsEntity displayColumns = new DisplayColumnsEntity();
//    private String baseUrl = System.getProperty("baseUrl");

//    public void resetDisplayedColumns() {
//        String userName = "qa-automation-01@apriori.com";
//
//        new HTTPRequest()
//            .unauthorized()
//            .customizeRequest()
//            .setHeaders(apiAuthentication.initAuthorizationHeader(userName))
//            .setEndpoint(baseUrl + "ws/workspace/users/me/table-views/workspaceTable")
//            .setBody(displayColumns
//                .setChildren(generateDefaultColumnsList())
//                .setName("private"))
//            .commitChanges()
//            .connect()
//            .post();
//    }

//    private List<ColumnEntity> generateDefaultColumnsList() {
//        List<ColumnEntity> columns = new ArrayList<>();
//        ColumnEntity column;
//
//        column = new ColumnEntity();
//        column.setEditable(false);
//        column.setName("thumbnail");
//        column.setDisplayName("Thumbnail");
//        columns.add(column);
//
//        column = new ColumnEntity();
//        column.setEditable(false);
//        column.setName("scenarioKey");
//        column.setDisplayName("Name / Scenario");
//        columns.add(column);
//
//        column = new ColumnEntity();
//        column.setEditable(false);
//        column.setName("costStatus");
//        column.setDisplayName("Locked / Workspace");
//        columns.add(column);
//
//        column = new ColumnEntity();
//        column.setEditable(false);
//        column.setName("properties.processGroupDisplayName");
//        column.setDisplayName("Process Group");
//        columns.add(column);
//
//        column = new ColumnEntity();
//        column.setEditable(false);
//        column.setName("properties.vpeName");
//        column.setDisplayName("VPE");
//        columns.add(column);
//
//        column = new ColumnEntity();
//        column.setEditable(false);
//        column.setName("properties.lastModified");
//        column.setDisplayName("Last Saved");
//        columns.add(column);
//
//        return columns;
//    }
}
