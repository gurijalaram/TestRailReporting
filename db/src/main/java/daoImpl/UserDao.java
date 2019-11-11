package daoImpl;

import dao.GlobalDao;
import entity.User;
import entity.UserGroups;
import utils.SessionFactoryClass;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;

import java.util.ArrayList;
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
            for (int i = 0; i < usersList.size(); i++) {
                session.saveOrUpdate(usersList.get(i));
            }
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
        List<UserGroups> userGroups = new ArrayList<UserGroups>();
        try {
            transaction = session.beginTransaction();
            for (int i = 0; i < dbObject.size(); i++) {
                userGroups = new UserGroupsDao(session).getByFullName(dbObject.get(i));
                for (int z = 0; z < userGroups.size(); z++) {
                    session.delete(userGroups.get(z));
                }
                session.delete(dbObject.get(i));
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
