package daoImpl;

import dao.GlobalDao;
import entity.User;
import org.hibernate.Session;

import org.hibernate.Query;
import java.util.List;

public class UserDao extends GlobalDao <User> {
    public UserDao(Session session) {
        super(session);
    }

    public User getByFullName(User user) {
        Query query = session.createQuery("FROM User u where fullName=:userfullName").setParameter("userfullName", user.getFullName());
        List<User> dbListObjects = query.list();
        return dbListObjects.get(0);
    }
}
