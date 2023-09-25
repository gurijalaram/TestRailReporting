package com.integration.tests.customer.applications.cir;

import com.apriori.enums.ReportNamesEnum;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class CIReportApplicationTest extends TestBaseUI {

    @Test
    @TestRail(id = {})
    @Description("")
    public void testUserLoginCIR() {
        CommonReportTests commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

}
