package com.api;

import com.apriori.api.entity.reponse.UploadComponent;
import com.apriori.api.objects.CidApiObject;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import utils.Constants;

public class CidApiTests extends TestUtil {

    private ResponseWrapper<Object> imageViewer;

    @Test
    @Description("Check the cad viewer values are correct")
    public void cadViewerValues() {
        String apiUrl = String.format(Constants.getApiUrl(), "components");

        imageViewer = new CidApiObject().getToken(Constants.getSecretKey(),
            Constants.getCidServiceHost(),
            HttpStatus.SC_CREATED,
            Constants.getCidTokenUsername(),
            Constants.getCidTokenEmail(),
            Constants.getCidTokenIssuer(),
            Constants.getCidTokenSubject())
            .uploadFile(apiUrl, UploadComponent.class, new GenerateStringUtil().generateScenarioName(), "bracket_basic.prt");
    }
}
