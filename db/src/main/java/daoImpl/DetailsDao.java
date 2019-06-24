package daoImpl;

import dao.GlobalDao;
import entity.Detail;
import org.hibernate.Session;

public class DetailsDao extends GlobalDao<Detail> {
    public DetailsDao(Session session) {
        super(session);
    }
}
