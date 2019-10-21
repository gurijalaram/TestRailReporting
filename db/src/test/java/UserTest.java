import daoImpl.UserDao;
import entity.User;
import org.junit.Test;
import utils.PropertiesHandler;
import utils.SessionFactoryClass;

import java.util.ArrayList;
import java.util.List;

public class UserTest {

    static {
        /* set the type of db, for all tests: mysql/mssql/oracle */
        new PropertiesHandler().setDBProperties("mysql");
    }

    @Test
    public void testGetAllUsersFromDB() {
        /*Get all users from DB*/
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
        
        /*Create new user with FullName: aPriori Default User and RawLoginID: adu*/
        User user = new User().setFullName("aPriori Default User").setRawLoginID("adu");
        userForCreate.add(user);
        userDao.create(userForCreate);
    }

    @Test
    public void testUpdateUser() {
        UserDao userDao = new UserDao(new SessionFactoryClass(User.class).getSession());
        List<User> userForDelete = new ArrayList<User>();
        
        /*Change FullName of use "aPriori Default User" to "aPriori Test User" */
        User user = new User().setFullName("aPriori Default User");
        userForDelete.add(userDao.getByFullName(user).setFullName("aPriori Test User"));
        userDao.update(userForDelete);
    }

    @Test
    public void testDeleteUser() {
        UserDao userDao = new UserDao(new SessionFactoryClass(User.class).getSession());
        List<User> userForDelete = new ArrayList<User>();

        /*Remove user with name "aPriori Default User" and RawLoginID "adu" from DB*/
        User user = new User().setFullName("aPriori Default User").setRawLoginID("adu");
        userForDelete.add(user);
        userDao.delete(userDao.getAllObjects(userForDelete.get(0).getClass()));
    }

    @Test
    public void testGetUserByNameFromDB() {
        UserDao userDao = new UserDao(new SessionFactoryClass(User.class).getSession());
        /*Get single use by its name*/
        User user = new User().setFullName("Salvador Sakho");
        System.out.println(userDao.getByFullName(user).getUser_ID());
    }
}
