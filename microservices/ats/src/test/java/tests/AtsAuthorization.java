package tests;

import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.utils.AuthorizeUserUtil;
import com.apriori.ats.utils.Constants;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class AtsAuthorization extends TestUtil {

    @Test
    @TestRail(testCaseId = {"3581"})
    @Description("Retrieve a JWT from the ATS Token endpoint")
    public void getToken() {
        new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
                Constants.getAtsServiceHost(),
                HttpStatus.SC_CREATED,
            Constants.getAtsTokenUsername(),
            Constants.getAtsTokenEmail(),
            Constants.getAtsTokenIssuer(),
            Constants.getAtsTokenSubject());
    }

    @Test
    @TestRail(testCaseId = {"3913"})
    @Description("Authorize a user to access a specified application")
    public void authorizeUser() {
        String token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
            Constants.getAtsServiceHost(),
                HttpStatus.SC_CREATED,
            Constants.getAtsTokenUsername(),
            Constants.getAtsTokenEmail(),
            Constants.getAtsTokenIssuer(),
            Constants.getAtsTokenSubject());


        AuthorizationResponse response = AuthorizeUserUtil.authorizeUser(Constants.getSecretKey(),
            Constants.getAtsServiceHost(),
            Constants.getAtsAuthTargetCloudContext(),
                token,
                HttpStatus.SC_OK);
    }
}
