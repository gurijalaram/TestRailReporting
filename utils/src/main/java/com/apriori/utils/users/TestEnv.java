package com.apriori.utils.users;

import org.junit.Test;


//TODO z: USE IT to test different cases before merge PR, to avoid bugs\\DELETE BEFORE MERGE
public class TestEnv {

    @Test
    public void FailedIf() {
//        System.setProperty("env", "cid-te");
//
        System.out.println(UserUtil.getUser().getUsername());
        System.out.println(UserUtil.getUser().getUsername());
//        System.out.println(UserUtil.getNewUser().getUsername());
//        System.out.println(UserUtil.getNewUser().getUsername());
//        System.out.println(UserUtil.getNewUser().getUsername());
//        System.out.println(UserUtil.getNewUser().getUsername());

        System.out.println("COMMON user" + UserUtil.getUser("common").getUsername());
        System.out.println(UserUtil.getUser("common").getUsername());
        System.out.println(UserUtil.getUser("common").getUsername());

//        System.out.println("USERS: " + UserUtil.getUser().getUsername() + "              " + UserUtil.getUser().getPassword());
//        System.out.println("USERS: " + UserUtil.getUser().getUsername() + "              " + UserUtil.getUser().getPassword());
//        System.out.println("USERS: " + UserUtil.getUser().getUsername() + "              " + UserUtil.getUser().getPassword());
//        System.out.println("USERS: " + UserUtil.getUser().getUsername() + "              " + UserUtil.getUser().getPassword());
//        System.out.println("USERS: " + UserUtil.getUser().getUsername() + "              " + UserUtil.getUser().getPassword());
//        System.out.println("USERS: " + UserUtil.getUser().getUsername() + "              " + UserUtil.getUser().getPassword());


    }

}
