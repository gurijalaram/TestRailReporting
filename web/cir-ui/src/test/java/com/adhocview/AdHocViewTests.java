package com.adhocview;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.create.CreateAdHocViewPage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;
import testsuites.suiteinterface.ReportsTest;

public class AdHocViewTests extends TestBase {

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "12517")
    @Description("Verify Create Simple Ad Hoc View Report")
    public void testAdHocViewCreation() {
        CreateAdHocViewPage createAdHocViewPage = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateAdHocViewPage()
            .clickFirstDataSource()
            .clickChooseData()
            .waitForChooseDataDialogToAppear()
            .clickMoveDataToRightButton()
            .clickGoToDesignerOkButton()
            .changeVisualizationToTable()
            .changeDataScopeToFull()
            .addDataToTable()
            .addFilterToTable();

        assertThat(createAdHocViewPage.getTableCellValue("1", "1"),
            is(equalTo("3538968"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("1", "2"),
            is(equalTo("Initial"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("1", "3"),
            is(equalTo("5.68"))
        );

        assertThat(createAdHocViewPage.getTableCellValue("14", "1"),
            is(equalTo("TOP-LEVEL"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("14", "2"),
            is(equalTo("Initial"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("14", "3"),
            is(equalTo("27.17"))
        );
    }
}
