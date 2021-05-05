package com.apriori.vds.tests.util;

import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import com.apriori.vds.utils.Constants;

public class VDSTestUtil extends TestUtil {

    protected static String token;

    @BeforeClass
    public static void initTestingComponentInfo() {
        initToken();
    }

    private static String initToken() {
        if (token == null) {
            token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
                Constants.getCidServiceHost(),
                HttpStatus.SC_CREATED,
                Constants.getCidTokenUsername(),
                Constants.getCidTokenEmail(),
                Constants.getCidTokenIssuer(),
                Constants.getCidTokenSubject());
        }

        return token;
    }
}
