package daoImpl;

import dao.GlobalDao;
import entity.User;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class UserDao extends GlobalDao <User> {
    public UserDao(Session session) {
        super(session);
    }

    public User getByFullName(User user) {
        Query query = session.createQuery("FROM " + User.class.getName() + " u where u.fullName =: userfullName").setParameter("userfullName", user.getFullName());
        List<User> dbListObjects = query.getResultList();
        return dbListObjects.get(0);
    }
}
