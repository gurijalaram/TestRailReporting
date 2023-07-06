package com.customer.systemconfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.utils.common.customer.response.Customer;
import com.apriori.utils.common.customer.response.Sites;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customer.systemconfiguration.SystemConfigurationPage;
import com.apriori.login.CasLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class SystemConfigurationTests extends TestBase {
    private SystemConfigurationPage systemConfigurationPage;
    private Customer aprioriInternal;
    private CdsTestUtil cdsTestUtil;

    @Before
    public void setup() {

        systemConfigurationPage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openAprioriInternal()
                .goToSystemConfiguration();
    }

    @Test
    @TestRail(testCaseId = {"10977", "10978", "10979"})
    @Description("Validate aPriori Internal Site and deployment can be selected in dropdown on System Configuration page")
    public void validateSelectSiteAndDeployment() {
        cdsTestUtil = new CdsTestUtil();
        aprioriInternal = cdsTestUtil.getAprioriInternal();
        String customerIdentity = aprioriInternal.getIdentity();
        ResponseWrapper<Sites> aprioriInternalSites = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES_BY_CUSTOMER_ID, Sites.class, HttpStatus.SC_OK, customerIdentity);
        String internalSiteName = aprioriInternalSites.getResponseEntity().getItems().get(0).getName();
        String siteName2 = aprioriInternalSites.getResponseEntity().getItems().get(1).getName();

        assertThat(systemConfigurationPage.getSiteInDropDown(), is(equalTo(internalSiteName)));
        assertThat(systemConfigurationPage.getDeploymentInDropDown(), is(equalTo("PRODUCTION")));

        systemConfigurationPage.selectSite(siteName2)
                .goToPermissionsPage();

        assertThat(systemConfigurationPage.getSiteInDropDown(), is(equalTo(siteName2)));

        systemConfigurationPage.selectSite(internalSiteName)
                .selectDeployment("Preview")
                .goToGroupsPage();

        assertThat(systemConfigurationPage.getSiteInDropDown(), is(equalTo(internalSiteName)));
        assertThat(systemConfigurationPage.getDeploymentInDropDown(), is(equalTo("Preview")));
    }
}