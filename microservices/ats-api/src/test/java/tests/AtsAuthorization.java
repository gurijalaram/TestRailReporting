package tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.utils.AuthorizeUserUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class AtsAuthorization extends TestUtil {

    @Test
    @TestRail(testCaseId = {"3581"})
    @Description("Retrieve a JWT from the ATS Token endpoint")
    public void getToken() {
        new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"3913"})
    @Description("Authorize a user to access a specified application")
    public void authorizeUser() {
        String token = new JwtTokenUtil().retrieveJwtToken();


        AuthorizationResponse response = AuthorizeUserUtil.authorizeUser(PropertiesContext.getStr("${env}.secret_key"),
            PropertiesContext.getStr("${env}.ats.api_url"),
            PropertiesContext.getStr("${env}.ats.ats_auth_targetCloudContext"),
            token,
            HttpStatus.SC_OK);
    }
}
