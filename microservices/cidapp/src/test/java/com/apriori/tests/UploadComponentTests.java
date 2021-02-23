package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.ComponentsController;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.reponse.ComponentIdentityResponse;
import com.apriori.entity.reponse.upload.UploadComponentResponse;
import com.apriori.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class UploadComponentTests extends TestUtil {

    private static String token;

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
    public void boundingBoxValues() {
        ResponseWrapper<PostComponentResponse> response = new ComponentsController().postComponents(token, new GenerateStringUtil().generateScenarioName(), "bracket_basic.prt");

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }

    @Test
    @Description
    public void getComponentIdentity() {
        final String url = String.format(Constants.getApiUrl(), "components");
        final String partName = "bracket_basic.prt";

        RequestEntity requestEntity = RequestEntity.init(url, UploadComponentResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(partName)))
            .setFormParams(new FormParams().use("filename", partName)
                .use("override", "false")
                .use("scenarioName", new GenerateStringUtil().generateScenarioName()));

        ResponseWrapper<UploadComponentResponse> uploadComponentResponse = GenericRequestUtil.post(requestEntity, new RequestAreaApi());

        String componentIdentity = uploadComponentResponse.getResponseEntity().getComponentIdentity();

        final String IdentityUrl = String.format(Constants.getApiUrl(), "components/" + componentIdentity);

        RequestEntity identityRequestEntity = RequestEntity.init(IdentityUrl, ComponentIdentityResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        ResponseWrapper<ComponentIdentityResponse> componentIdentityResponse = GenericRequestUtil.get(identityRequestEntity, new RequestAreaApi());

        assertThat(componentIdentityResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIdentityResponse.getResponseEntity().getResponse().getIdentity(), is(equalTo(componentIdentity)));
    }
}
