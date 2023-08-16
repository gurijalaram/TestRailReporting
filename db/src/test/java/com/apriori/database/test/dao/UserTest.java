package com.apriori.database.test.dao;

import com.apriori.database.dao.UserDao;
import com.apriori.database.entity.User;
import com.apriori.database.utils.PropertiesHandler;
import com.apriori.database.utils.SessionFactoryClass;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UserTest {

    static {
        /* set the type of db, for all tests: mysql/mssql/oracle */
        new PropertiesHandler().setDBProperties("mysql");
    }

    @Test
    public void testGetAllUsersFromDB() {
        /*Get all users from DB*/
        UserDao userDao = new UserDao(new SessionFactoryClass().getSession());
        User user = new User();
        for (User userFromList : userDao.getAllObjects(user.getClass())) {
            System.out.println(userFromList.getFullName());
        }
    }

    @Test
    public void testCreateUser() {
        UserDao userDao = new UserDao(new SessionFactoryClass().getSession());
        List<User> usersForCreate = new ArrayList<>();
        
        /*Create new user with FullName: aPriori Default User and RawLoginID: adu*/
        User user = new User()
                .setFullName("aPriori Default User")
                .setRawLoginID("adu");
        usersForCreate.add(user);
        userDao.create(usersForCreate);

    }

    @Test
    public void testUpdateUser() {
        UserDao userDao = new UserDao(new SessionFactoryClass().getSession());

        List<User> usersForUpdate = new ArrayList<>();
        
        /*Change FullName of use "aPriori Default User" to "aPriori Test User" */
        User user = new User().setFullName("aPriori Default User");
        usersForUpdate.add(userDao.getByFullName(user).setFullName("aPriori Test User"));
        userDao.update(usersForUpdate);
    }

    @Test
    public void testDeleteUser() {
        UserDao userDao = new UserDao(new SessionFactoryClass().getSession());
        List<User> userForDelete = new ArrayList<>();

        /*Remove user with name "aPriori Default User" and RawLoginID "adu" from DB*/
        User user = new User().setFullName("aPriori Test User").setRawLoginID("adu");
        userForDelete.add(userDao.getByFullName(user));
        userDao.delete(userForDelete);
    }

    @Test
    public void testGetUserByNameFromDB() {
        UserDao userDao = new UserDao(new SessionFactoryClass().getSession());
        /*Get single use by its name*/
        User user = new User().setFullName("aPriori Test User");
        System.out.println(userDao.getByFullName(user).getUserId());
    }
}
