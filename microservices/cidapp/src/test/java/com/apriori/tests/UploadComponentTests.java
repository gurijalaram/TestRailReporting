package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.ComponentsController;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.reponse.upload.UploadComponentResponse;
import com.apriori.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class UploadComponentTests extends TestUtil {

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

        image = new ComponentsController().postComponents(token, apiUrl, UploadComponentResponse.class, new GenerateStringUtil().generateScenarioName(), "bracket_basic.prt");

        assertThat(image.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }
}
