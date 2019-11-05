package daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.GlobalDao;
import entity.User;
import entity.UserGroups;
import utils.SessionFactoryClass;

public class UserGroupsDao
    extends GlobalDao<UserGroups> {

    public UserGroupsDao(Session session) {
        super(session);
    }

    public UserGroupsDao() {
    }

    public UserGroups getByFullName(User user) {
        Query query =
            session.createQuery("FROM UserGroups ug, User u where fullName=:userfullName and ug.user_ID = u.user_ID").setParameter("userfullName",
                                                                                                                                   user.getFullName());
        List<UserGroups> dbListObjects = query.list();
        return dbListObjects.get(0);
    }

    public void addUsertoGroup(List<User> users) {
        /*
         * To have all rights User have to have: 
         * 1) licenseId (based on license type, application features will be available) 
         * 2) Record in table: fbc_schemadescr, with necessary rights: 
         * 2.1) schemaGroup - 246127cd-e5e1-4deb-b03f-b6eaec406ddc - ??? 
         * 2.2) schemaGroup - c3d6cac1-71a9-4248-a599-f728da5d018f - ??? 
         * 2.3) namedGroup - 5154a89b-157b-4180-8833-7c40e712f9ed - all users 
         * 2.4) namedGroup - e2e4cbe5-5ded-4120-a5ac-75568b0a989d - super user
         */
        
        Transaction transaction = session.beginTransaction();
        try {
            for (int i = 0; i < users.size(); i++) {
                UserGroups schemaGroup = new UserGroups(users.get(i).getUser_ID(), "schemaGroup", "246127cd-e5e1-4deb-b03f-b6eaec406ddc");
                session.save(schemaGroup);
                UserGroups schemaGroup1 = new UserGroups(users.get(i).getUser_ID(), "schemaGroup", "c3d6cac1-71a9-4248-a599-f728da5d018f");
                session.save(schemaGroup1);
                UserGroups namedGroupAllUsers =
                    new UserGroups(users.get(i).getUser_ID(), "namedGroup", "5154a89b-157b-4180-8833-7c40e712f9ed");
                session.save(namedGroupAllUsers);
                UserGroups namedGroupSuperUser =
                    new UserGroups(users.get(i).getUser_ID(), "namedGroup", "e2e4cbe5-5ded-4120-a5ac-75568b0a989d");
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
