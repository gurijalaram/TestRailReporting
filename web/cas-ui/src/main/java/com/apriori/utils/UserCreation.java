package com.apriori.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.utils.CdsTestUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            User added = cdsTestUtil.addUser(customerIdentity, username, customerName).getResponseEntity();
            sourceUsers.add(added);
        }
        return sourceUsers;
    }
}