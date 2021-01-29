package com.api;

import com.apriori.api.entity.reponse.UploadComponent;
import com.apriori.api.objects.CidApiObject;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.Constants;

public class CidApiTests extends TestUtil {

    private static String token;
    private ResponseWrapper<Object> image;

    @BeforeClass
    public static void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
            Constants.getCidServiceHost(),
            HttpStatus.SC_CREATED,
            Constants.getCidTokenUsername(),
            Constants.getCidTokenEmail(),
            Constants.getCidTokenIssuer(),
            Constants.getCidTokenSubject());
    }

    @Test
    @Description("Check the cad viewer values are correct")
    public void cadViewerValues() {
        final String apiUrl = String.format(Constants.getApiUrl(), "components");

        image = new CidApiObject().uploadFile(token, apiUrl, UploadComponent.class, new GenerateStringUtil().generateScenarioName(), "bracket_basic.prt");

    }
}
