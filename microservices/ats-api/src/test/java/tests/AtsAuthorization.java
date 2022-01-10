package tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.utils.AuthorizeUserUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
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

        ResponseWrapper<AuthorizationResponse> response = AuthorizeUserUtil.authorizeUser(
            PropertiesContext.get("${env}.auth_target_cloud_context"), token);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
