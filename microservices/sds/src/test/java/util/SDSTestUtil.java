package util;

import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.sds.utils.Constants;

import org.apache.http.HttpStatus;
import org.junit.BeforeClass;

public class SDSTestUtil extends TestUtil {

    protected static String token;
    protected static final String COMPONENT_ID = "5A8GN7EB6EH3";
    protected static final String SCENARIO_ID = "5A5N4ECAJ2EJ";

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
}
