package com.apriori.vds.api.tests;

import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariable;
import com.apriori.vds.api.tests.util.ProcessGroupUtil;
import com.apriori.vds.api.tests.util.SiteVariableUtil;

import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(TestRulesAPI.class)
public class ProcessGroupSiteVariablesTest {
    protected static final Set<String> siteVariableNamesToDelete = new HashSet<>();
    private SiteVariableUtil siteVariableUtil;
    private RequestEntityUtil requestEntityUtil;
    private ProcessGroupUtil processGroupUtil;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
        siteVariableUtil = new SiteVariableUtil(requestEntityUtil);
        processGroupUtil = new ProcessGroupUtil(requestEntityUtil);
    }

    @AfterEach
    public void deleteTestingData() {
        siteVariableNamesToDelete.forEach(site -> siteVariableUtil.deleteProcessGroupSiteVariableByName(site));
    }

    @Test
    @TestRail(id = {8288})
    @Description("DELETEs a site variable. ")
    public void deleteSiteVariablesByName() {
        siteVariableUtil.deleteProcessGroupSiteVariableByName(siteVariableUtil.putProcessGroupSiteVariables().getName());
    }

    @Test
    @TestRail(id = {8286})
    @Description("Adds or Replaces a CustomAttribute for a user. ")
    public void putCustomAttributeByIdentity() {
        SiteVariable siteVariableBeforeUpdate = siteVariableUtil.putProcessGroupSiteVariables();
        siteVariableNamesToDelete.add(siteVariableBeforeUpdate.getName());
        SiteVariable updatedSiteVariableResponse = siteVariableUtil.putSiteVariable(processGroupUtil.getProcessGroupIdentity(), siteVariableBeforeUpdate);

        siteVariableUtil.validateCreatedObject(updatedSiteVariableResponse);
    }
}
