import daoImpl.UserDao;
import entity.User;
import org.junit.Test;
import utils.PropertiesHendler;
import utils.SessionFactoryClass;

import java.util.ArrayList;
import java.util.List;

public class UserTest {

    static {
        /*
            set the type of db, for all tests:
             - mysql
             - mssql
             - oracle
        */
        new PropertiesHendler().setDBProperties("mssql");
    }

    @Test
    public void testGetAllUsersFromDB() {
        UserDao userDao = new UserDao(new SessionFactoryClass(User.class).getSession());
        User user = new User();
        for (User userFromList : userDao.getAllObjects(user.getClass())) {
            System.out.println(userFromList.getFullName());
        }
    }

    @Test
    public void testCreateUser() {
        UserDao userDao = new UserDao(new SessionFactoryClass(User.class).getSession());
        List<User> userForCreate = new ArrayList<User>();

        /* Users Examples */
        User user = new User().setFullName("aPriori Default User").setRawLoginID("adu");
        userForCreate.add(user);
        userDao.create(userForCreate);
    }

    @Test
    public void testUpdateUser() {
        UserDao userDao = new UserDao(new SessionFactoryClass(User.class).getSession());
        List<User> userForDelete = new ArrayList<User>();

        /* Users Examples */
        User user = new User().setFullName("aPriori Default User");
        /* Currently didn't find another way, of how to update multiple entities.
        getByFullName - returns entity which should be updated. */
        userForDelete.add(userDao.getByFullName(user).setFullName("aPriori Test User"));
        userDao.update(userForDelete);
    }

    @Test
    public void testDeleteUser() {
        UserDao userDao = new UserDao(new SessionFactoryClass(User.class).getSession());
        List<User> userForDelete = new ArrayList<User>();

        /* Users Examples */
        User user = new User().setFullName("aPriori Default User").setRawLoginID("adu");

        userForDelete.add(user);
        userDao.delete(userDao.getAllObjects(userForDelete.get(0).getClass()));
    }

    @Test
    public void testGetUserByNameFromDB() {
        UserDao userDao = new UserDao(new SessionFactoryClass(User.class).getSession());
        User user = new User().setFullName("Salvador Sakho");
        System.out.println(userDao.getByFullName(user).getUser_ID());
    }
}
