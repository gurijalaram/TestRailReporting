package com.adhocview;

import static com.apriori.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.pageobjects.create.CreateAdHocViewPage;
import com.apriori.pageobjects.login.ReportsLoginPage;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class AdHocViewTests extends TestBaseUI {

    @Test
    @Tag(REPORTS)
    @TestRail(id = 12517)
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
            is(equalTo("0200613"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("1", "2"),
            is(equalTo("Initial"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("1", "3"),
            is(equalTo("4.35"))
        );

        assertThat(createAdHocViewPage.getTableCellValue("14", "1"),
            is(equalTo("TOP-LEVEL"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("14", "2"),
            is(equalTo("Initial"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("14", "3"),
            is(equalTo("27.28"))
        );
    }
}
