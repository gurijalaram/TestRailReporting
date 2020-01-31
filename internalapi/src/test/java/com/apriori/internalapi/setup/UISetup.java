package com.apriori.internalapi.setup;


import com.apriori.apibase.http.builder.common.response.common.ColumnEntity;
import com.apriori.apibase.http.builder.common.response.common.DisplayColumnsEntity;
import com.apriori.pageobjects.utils.APIAuthentication;

import io.qameta.allure.Issue;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility showing adding columns to CI Design Explore view using API
 */
public class UISetup {

    private DisplayColumnsEntity displayColumns = new DisplayColumnsEntity();

    @Issue("BA-915")
    public void resetDisplayedColumns() {
        String userName = "qa-automation-01@apriori.com";

        new APIAuthentication(userName, "ws/workspace/users/me/table-views/workspaceTable").requestAuthorisation()
            .setBody(displayColumns
                .setChildren(generateDefaultColumnsList())
                .setName("private"))
            .commitChanges()
            .connect()
            .post();
    }

    private List<ColumnEntity> generateDefaultColumnsList() {
        List<ColumnEntity> columns = new ArrayList<ColumnEntity>();
        ColumnEntity column;

        column = new ColumnEntity();
        column.setEditable(false);
        column.setName("thumbnail");
        column.setDisplayName("Thumbnail");
        columns.add(column);

        column = new ColumnEntity();
        column.setEditable(false);
        column.setName("scenarioKey");
        column.setDisplayName("Name / Scenario");
        columns.add(column);

        column = new ColumnEntity();
        column.setEditable(false);
        column.setName("costStatus");
        column.setDisplayName("Locked / Workspace");
        columns.add(column);

        column = new ColumnEntity();
        column.setEditable(false);
        column.setName("properties.processGroupDisplayName");
        column.setDisplayName("Process Group");
        columns.add(column);

        column = new ColumnEntity();
        column.setEditable(false);
        column.setName("properties.vpeName");
        column.setDisplayName("VPE");
        columns.add(column);

        column = new ColumnEntity();
        column.setEditable(false);
        column.setName("properties.lastModified");
        column.setDisplayName("Last Saved");
        columns.add(column);

        return columns;
    }
}
