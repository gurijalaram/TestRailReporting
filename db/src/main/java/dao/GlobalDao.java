package dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.IntStream;

public abstract class GlobalDao<T> {

    protected Session session;

    public GlobalDao(Session session) {
        this.session = session;
    }

    public GlobalDao() {
    }

    public List<T> getAllObjects(Class<?> dbObject) {
        Query query = session.createQuery("FROM " + dbObject.getName());
        return query.list();
    }

    public void update(List<T> dbObject) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            IntStream.range(0, dbObject.size()).forEach(i -> session.saveOrUpdate(dbObject.get(i)));
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(List<T> dbObject) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            IntStream.range(0, dbObject.size()).forEach(i -> session.delete(dbObject.get(i)));
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(List<T> dbObject) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            IntStream.range(0, dbObject.size()).forEach(i -> session.save(dbObject.get(i)));
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
