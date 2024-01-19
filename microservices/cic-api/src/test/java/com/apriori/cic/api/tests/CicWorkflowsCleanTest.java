package com.apriori.cic.api.tests;

import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CicWorkflowsCleanTest extends WorkflowTestUtil {

    private SoftAssertions softAssertions;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
    }

    @Test
    @Description("Delete all workflows starts with - and CIC")
    @TestRail(id = {5579})
    public void testCleanWorkflows() {
        softAssertions.assertThat(this.cicLogin().isWorkflowsDeleted()).isTrue();
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
    }
}
