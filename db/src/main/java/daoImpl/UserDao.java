package daoImpl;

import dao.GlobalDao;
import entity.User;
import org.hibernate.Session;

public class UserDao extends GlobalDao <User> {
    public UserDao(Session session) {
        super(session);
    }
}
