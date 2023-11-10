package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.enums.BcmAppAPIEnum;
import com.apriori.bcm.api.models.request.Worksheet;
import com.apriori.bcm.api.models.request.WorksheetRequest;
import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
    }
}
