package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorkSheets;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class WorksheetTests extends  BcmUtil {
    private final BcmUtil bcmUtil = new BcmUtil();
    private static SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = 28963)
    @Description("Verify worksheet creation")
    public void verifyWorksheetCreation() {
        String name =  new GenerateStringUtil().saltString("name");

        ResponseWrapper<WorkSheetResponse> response = bcmUtil.createWorksheet(name);
        softAssertions.assertThat(response.getResponseEntity().getName()).isEqualTo(name);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29156)
    @Description("Verify worksheet creation - already exists error")
    public void verifyWorksheetCreationAlreadyExistError() {
        String name =  new GenerateStringUtil().saltString("name");

        ResponseWrapper<WorkSheetResponse> response = bcmUtil.createWorksheet(name);
        softAssertions.assertThat(response.getResponseEntity().getName()).isEqualTo(name);

        ResponseWrapper<ErrorResponse> responseError = bcmUtil.createWorksheetAlreadyExists(name);
        softAssertions.assertThat((responseError.getResponseEntity().getMessage()).contains("already exists"));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29276)
    @Description("Verify worksheet list is returned")
    public void verifyWorksheetList() {
        String name =  new GenerateStringUtil().saltString("name");

        bcmUtil.createWorksheet(name);

        ResponseWrapper<WorkSheets> worksheetsList = bcmUtil.getWorksheetsList();
        softAssertions.assertThat(worksheetsList.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(worksheetsList.getResponseEntity().getItems().stream().filter(worksheet -> worksheet.getName().equals(name)).collect(Collectors.toList()).get(0)).isNotNull();
        softAssertions.assertAll();
    }
}
