package com.apriori.database.dao;

import com.apriori.database.entity.User;
import com.apriori.database.entity.UserGroups;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDao
        extends GlobalDao<User> {
    public UserDao(Session session) {
        super(session);
    }

    @Override
    public void create(List<User> usersList) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            usersList.forEach(user -> session.saveOrUpdate(user));
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserGroupsDao userGroupsDao = new UserGroupsDao(session);
        userGroupsDao.addUsertoGroup(usersList);
    }

    @Override
    public void delete(List<User> dbObject) {
        Transaction transaction;
        List<UserGroups> userGroups;
        try {
            transaction = session.beginTransaction();
            for (User user : dbObject) {
                userGroups = new UserGroupsDao(session).getByFullName(user);
                userGroups.forEach(userGroup -> session.delete(userGroup));
                session.delete(user);
                // session.delete(dbObject.get(i));
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getByFullName(User user) {
        Query query = session.createQuery("FROM User u where fullName=:userfullName").setParameter("userfullName", user.getFullName());
        List<User> dbListObjects = query.list();
        return dbListObjects.get(0);
    }
}
