package dao;

import entity.User;
import entity.UserGroups;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserGroupsDao
        extends GlobalDao<UserGroups> {

    public UserGroupsDao(Session session) {
        super(session);
    }

    public UserGroupsDao() {
    }

    public List<UserGroups> getByFullName(User user) {
        UserDao userDao = new UserDao(session);
        Query query =
                session.createQuery("FROM UserGroups ug " + "where ug.user_ID=:user_ID")
                        .setParameter("user_ID", userDao.getByFullName(user).getUserId());
        return query.list();
    }

    public void addUsertoGroup(List<User> users) {
        /*
         * To have all rights User have to have: 1) licenseId (based on license type, application features will be
         * available) 2) Record in table: fbc_schemadescr, with necessary rights: 2.1) schemaGroup -
         * 246127cd-e5e1-4deb-b03f-b6eaec406ddc - ??? 2.2) schemaGroup - c3d6cac1-71a9-4248-a599-f728da5d018f - ??? 2.3)
         * namedGroup - 5154a89b-157b-4180-8833-7c40e712f9ed - all users 2.4) namedGroup -
         * e2e4cbe5-5ded-4120-a5ac-75568b0a989d - super user
         */

        Transaction transaction = session.beginTransaction();
        try {
            for (User user : users) {
                UserGroups schemaGroup = new UserGroups(user.getUserId(), "schemaGroup", "246127cd-e5e1-4deb-b03f-b6eaec406ddc");
                session.save(schemaGroup);
                UserGroups schemaGroup1 = new UserGroups(user.getUserId(), "schemaGroup", "c3d6cac1-71a9-4248-a599-f728da5d018f");
                session.save(schemaGroup1);
                UserGroups namedGroupAllUsers =
                    new UserGroups(user.getUserId(), "namedGroup", "5154a89b-157b-4180-8833-7c40e712f9ed");
                session.save(namedGroupAllUsers);
                UserGroups namedGroupSuperUser =
                    new UserGroups(user.getUserId(), "namedGroup", "e2e4cbe5-5ded-4120-a5ac-75568b0a989d");
                session.save(namedGroupSuperUser);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

    }
}
