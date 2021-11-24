package tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.utils.AuthorizeUserUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class AtsAuthorization extends TestUtil {


    @Test
    public void getTo() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        String componentName2 = "Push Pin";
        FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
    }


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

        AuthorizationResponse response = AuthorizeUserUtil.authorizeUser(
            PropertiesContext.get("${env}.auth_target_cloud_context"),
            token,
            HttpStatus.SC_OK);
    }
}
