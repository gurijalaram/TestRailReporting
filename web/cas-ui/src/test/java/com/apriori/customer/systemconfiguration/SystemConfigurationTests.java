package com.apriori.customer.systemconfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.apriori.TestBaseUI;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Sites;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.pageobjects.customer.systemconfiguration.SystemConfigurationPage;
import com.apriori.pageobjects.login.CasLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SystemConfigurationTests extends TestBaseUI {
    private SystemConfigurationPage systemConfigurationPage;
    private Customer aprioriInternal;
    private CdsTestUtil cdsTestUtil;

    @BeforeEach
    public void setup() {

        systemConfigurationPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openAprioriInternal()
                .goToSystemConfiguration();
    }

    @Test
    @TestRail(id = {10977, 10978, 10979})
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