package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class UpdateWorksheetTests extends BcmUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();

    // TODO - add clean up method once API will be available

    @Test
    @TestRail(id = {29487})
    @Description("Verify updating a Bulk Analysis worksheet")
    public void updateWorksheet() {
        String name = GenerateStringUtil.saltString("name");
        String updatedName = name + "updated";
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();

        WorkSheetResponse updatedWorksheet = updateWorksheet(updatedName, null, WorkSheetResponse.class, newWorksheet.getIdentity(), HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(updatedWorksheet.getName()).isEqualTo(updatedName);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29694})
    @Description("Verify negative cases of updating a Bulk Analysis worksheet")
    public void updateWorksheetNegativeTests() {
        String name = GenerateStringUtil.saltString("name");
        String updatedName = name + "updated";
        WorkSheetResponse existingWorksheet = getWorksheets().getResponseEntity().getItems().get(0);
        String existingNameWorksheet = existingWorksheet.getName();
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();
        String worksheetId = newWorksheet.getIdentity();

        ErrorResponse sameName = updateWorksheet(existingNameWorksheet, null, ErrorResponse.class, worksheetId, HttpStatus.SC_CONFLICT).getResponseEntity();

        softAssertions.assertThat(sameName.getMessage()).contains("already exists");

        ErrorResponse invalidIdentity = updateWorksheet(updatedName, null, ErrorResponse.class, "0000000", HttpStatus.SC_BAD_REQUEST).getResponseEntity();
        softAssertions.assertThat(invalidIdentity.getMessage()).isEqualTo("'identity' is not a valid identity.");

        ErrorResponse notExistingWorksheet = updateWorksheet(updatedName, null, ErrorResponse.class, "000000000000", HttpStatus.SC_NOT_FOUND).getResponseEntity();
        softAssertions.assertThat(notExistingWorksheet.getMessage()).contains("was not found");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29695})
    @Description("Verify updating worksheet with a description")
    public void updateWorksheetWithDescription() {
        String name = GenerateStringUtil.saltString("name");
        String description = new GenerateStringUtil().getRandomString();
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();

        WorkSheetResponse updatedWorksheet = updateWorksheet(null, description, WorkSheetResponse.class, newWorksheet.getIdentity(), HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(updatedWorksheet.getDescription()).isEqualTo(description);
        softAssertions.assertAll();
    }
}