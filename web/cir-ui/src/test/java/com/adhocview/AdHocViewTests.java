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
import org.openqa.selenium.By;
import testsuites.suiteinterface.ReportsTest;

public class AdHocViewTests extends TestBase {

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "0000")
    @Description("create test case and fill this and above line out properly later")
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

        assertThat(true, is(equalTo(true)));
        assertThat(
            driver.findElement(By.xpath("//tbody[@id='tableDetails']/tr[1]/td[1]/span")).getText(),
            is(equalTo("3538968"))
        );
        assertThat(
            driver.findElement(By.xpath("//tbody[@id='tableDetails']/tr[1]/td[2]/span")).getText(),
            is(equalTo("Initial"))
        );
        assertThat(
            driver.findElement(By.xpath("//tbody[@id='tableDetails']/tr[1]/td[3]/span")).getText(),
            is(equalTo("5.68"))
        );

        assertThat(
            driver.findElement(By.xpath("//tbody[@id='tableDetails']/tr[14]/td[1]/span")).getText(),
            is(equalTo("TOP-LEVEL"))
        );
        assertThat(
            driver.findElement(By.xpath("//tbody[@id='tableDetails']/tr[14]/td[2]/span")).getText(),
            is(equalTo("Initial"))
        );
        assertThat(
            driver.findElement(By.xpath("//tbody[@id='tableDetails']/tr[14]/td[3]/span")).getText(),
            is(equalTo("27.17"))
        );
    }
}
