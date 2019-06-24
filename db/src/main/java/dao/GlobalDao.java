package dao;

import org.hibernate.Session;

public abstract class GlobalDao<T> {
    protected Session session;

    public GlobalDao(Session session) {
        this.session = session;
    }

    public T get(T dbObject) {
        return session.get((Class<T>) dbObject, new Integer(1));
    }

    public void update(T dbObject) {
        session.update(dbObject);
    }

    public void delete(T dbObject){
        session.delete(dbObject);
    }
    public void create(T dbObject){
        session.delete(dbObject);
    }

}
