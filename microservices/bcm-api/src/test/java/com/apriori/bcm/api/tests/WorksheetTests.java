package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorkSheets;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ComponentResponse;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class WorksheetTests extends BcmUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private String worksheetIdentity;

    @AfterEach
    public void cleanUp() {
        if (worksheetIdentity != null) {
            deleteWorksheet(null, worksheetIdentity, HttpStatus.SC_NO_CONTENT);
        }
    }

    @Test
    @TestRail(id = 28963)
    @Description("Verify worksheet creation")
    public void verifyWorksheetCreation() {
        String name = new GenerateStringUtil().saltString("name");

        WorkSheetResponse response = createWorksheet(name);
        worksheetIdentity = response.getIdentity();
        softAssertions.assertThat(response.getName()).isEqualTo(name);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29156)
    @Description("Verify worksheet creation - already exists error")
    public void verifyWorksheetCreationAlreadyExistError() {
        String name = new GenerateStringUtil().saltString("name");

        WorkSheetResponse response = createWorksheet(name);
        worksheetIdentity = response.getIdentity();
        softAssertions.assertThat(response.getName()).isEqualTo(name);

        ResponseWrapper<ErrorResponse> responseError = createWorksheetAlreadyExists(name);
        softAssertions.assertThat(responseError.getResponseEntity().getMessage()).contains("already exists");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29276)
    @Description("Verify worksheet list is returned")
    public void verifyWorksheetList() {
        String name = new GenerateStringUtil().saltString("name");

        worksheetIdentity = createWorksheet(name).getIdentity();

        WorkSheets worksheetsList = getWorksheets().getResponseEntity();
        softAssertions.assertThat(worksheetsList.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(worksheetsList.getItems().stream().filter(worksheet -> worksheet.getName().equals(name)).collect(Collectors.toList()).get(0)).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29733)
    @Description("Verify getting specific worksheet")
    public void verifyGetSpecificWorkSheet() {
        WorkSheetResponse worksheetCreated =
            createWorksheet(GenerateStringUtil.saltString("name"));
        worksheetIdentity = worksheetCreated.getIdentity();

        ResponseWrapper<WorkSheetResponse> worksheetGet =
            getWorksheet(WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK);

        softAssertions.assertThat(worksheetGet.getResponseEntity())
            .isEqualTo(worksheetCreated);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29734)
    @Description("Verify getting specific worksheet 400 error identity is invalid")
    public void verifyGetWorkSheetWrongIdentity() {
        ResponseWrapper<ErrorResponse> worksheetGet =
            getWorksheet(ErrorResponse.class, "fake9876", HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(worksheetGet.getResponseEntity().getMessage())
            .isEqualTo("'identity' is not a valid identity.");
        softAssertions.assertThat(worksheetGet.getResponseEntity().getPath())
            .isEqualTo("/worksheets/fake9876");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29735)
    @Description("Verify getting specific worksheet 404 error worksheet does not exist")
    public void verifyGetWorkSheetDoesNotExist() {
        ResponseWrapper<ErrorResponse> error =
            getWorksheet(ErrorResponse.class, "CYTTG999999L", HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(error.getResponseEntity().getMessage())
            .isEqualTo("Resource 'Worksheet' with identity 'CYTTG999999L' was not found");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 30673)
    @Description("Get a filtered and sorted list of scenario iteration candidates")
    public void getCandidates() {
        String name = new GenerateStringUtil().saltString("name");

        worksheetIdentity = createWorksheet(name).getIdentity();

        ComponentResponse getCandidates = getCandidates(worksheetIdentity).getResponseEntity();
        softAssertions.assertThat(getCandidates.getItems()).isNotEmpty();

        ComponentResponse getFilteredCandidates = getCandidatesWithParams(worksheetIdentity, "componentType[IN]", "PART").getResponseEntity();
        softAssertions.assertThat(getFilteredCandidates.getItems().get(0).getComponentType()).isEqualTo("PART");

        ComponentResponse getSortedCandidates = getCandidatesWithParams(worksheetIdentity, "sortBy[DESC]", "scenarioCreatedAt").getResponseEntity();

        softAssertions.assertThat(getSortedCandidates.getItems()).isNotEmpty();
        softAssertions.assertAll();
    }
}
