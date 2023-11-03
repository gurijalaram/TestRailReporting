package com.apriori.cas.ui.utils;


import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserCreation extends TestUtil {

    private CdsTestUtil cdsTestUtil;
    private static final String STAFF_TEST_USER = "staff-test-user";

    /**
     * Creates customer users
     *
     * @param count - number of users
     * @param customerIdentity - identity of customer
     * @param customerName - customer name
     */
    public List<User> populateStaffTestUsers(int count, String customerIdentity, String customerName) {
        cdsTestUtil = new CdsTestUtil();
        List<User> sourceUsers = new ArrayList<>();

        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        for (int i = 0; i < count; ++i) {
            String username = String.format("%s-%s-%s", STAFF_TEST_USER, now, i);
            User added = cdsTestUtil.addUser(customerIdentity, username, customerName.toLowerCase()).getResponseEntity();
            sourceUsers.add(added);
        }
        return sourceUsers;
    }
}