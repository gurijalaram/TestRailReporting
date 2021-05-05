package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import io.qameta.allure.Description;
import org.junit.Test;

public class AccessControlsTest {

    @Test
    @TestRail(testCaseId = {"203486"})
    @Description("Get a list of Access Control Groups for a specific customer.")
    public void getGroups() {

    }

    @Test
    @TestRail(testCaseId = {"203487"})
    @Description("Get a list of Access Control Permissions for a specific customer.")
    public void getPermissions() {

    }

    @Test
    @TestRail(testCaseId = {"203488"})
    @Description("Post synchronize the access controls for this customer. ")
    public void postSynchronize() {

    }
}
