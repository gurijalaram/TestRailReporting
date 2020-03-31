package dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public abstract class GlobalDao<T> {

    protected Session session;

    public GlobalDao(Session session) {
        this.session = session;
    }

    public GlobalDao() {
    }

    public List<T> getAllObjects(Class<?> dbObject) {
        Query query = session.createQuery("FROM " + dbObject.getName());
        List<T> dbListObjects = query.list();
        return dbListObjects;
    }

    public void update(List<T> dbObject) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            for (int i = 0; i < dbObject.size(); i++) {
                session.saveOrUpdate(dbObject.get(i));
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(List<T> dbObject) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            for (int i = 0; i < dbObject.size(); i++) {
                session.delete(dbObject.get(i));
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(List<T> dbObject) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            for (int i = 0; i < dbObject.size(); i++) {
                session.save(dbObject.get(i));
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
