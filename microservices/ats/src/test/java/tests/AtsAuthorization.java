package tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.service.SecurityManager;
import com.apriori.ats.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.dao.ServiceConnector;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class AtsAuthorization extends TestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = ServiceConnector.getServiceUrl();
    }

    @Test
    @TestRail(testCaseId = "3581")
    @Description("Retrieve a JWT from the ATS Token endpoint")
    public void getToken() {
        SecurityManager.retrieveJwtToken(
                Constants.getAtsServiceHost(),
                HttpStatus.SC_CREATED,
            Constants.getAtsTokenUsername(),
            Constants.getAtsTokenEmail(),
            Constants.getAtsTokenIssuer(),
            Constants.getAtsTokenSubject());
    }

    @Test
    @TestRail(testCaseId = "3913")
    @Description("Authorize a user to access a specified application")
    public void authorizeUser() {
        String token = SecurityManager.retrieveJwtToken(
            Constants.getAtsServiceHost(),
                HttpStatus.SC_CREATED,
            Constants.getAtsTokenUsername(),
            Constants.getAtsTokenEmail(),
            Constants.getAtsTokenIssuer(),
            Constants.getAtsTokenSubject());


        AuthorizationResponse response = SecurityManager.authorizeUser(
            Constants.getAtsServiceHost(),
            Constants.getAtsAuthApplication(),
            Constants.getAtsAuthTargetCloudContext(),
                token,
                HttpStatus.SC_OK);
    }
}
