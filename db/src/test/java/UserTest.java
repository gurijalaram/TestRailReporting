import daoImpl.UserDao;
import entity.User;
import org.junit.Test;
import utils.SessionFactoryClass;

import java.util.ArrayList;
import java.util.List;

public class UserTest {

    @Test
    public void testGetAllUsersFromDB(){
        UserDao userDao = new UserDao(new SessionFactoryClass("mysql").getSession()); /// mysql - will be as parameter
        User user = new User();
        for (User userFromList : userDao.getAllObjects(user.getClass())){
            System.out.println(userFromList.getFullName());
        }
    }

    @Test
    public void testCreateUser(){
        UserDao userDao = new UserDao(new SessionFactoryClass("mysql").getSession()); /// mysql - will be as parameter
        List <User> userForDelete = new ArrayList<User>();

        /* Users Examples */
        User user = new User().setFullName("aPriori Default User").setRawLoginID("adu");
        User salvaUser = new User().setFullName("Salvador Sakho").setRawLoginID("ssakho");

        userForDelete.add(user);
        userForDelete.add(salvaUser);
        userDao.create(userForDelete);
    }

    /* Currently didn't find another way, of how to update multiple entities.
        getByFullName - returns entity which should be updated. */
    @Test
    public void testUpdateUser(){
        UserDao userDao = new UserDao(new SessionFactoryClass("mysql").getSession()); /// mysql - will be as parameter
        List <User> userForDelete = new ArrayList<User>();

        /* Users Examples */
        User user = new User().setFullName("aPriori User");
        User salvaUser = new User().setFullName("Salva");

        userForDelete.add(userDao.getByFullName(user).setFullName("aPriori Default User"));
        userForDelete.add(userDao.getByFullName(salvaUser).setFullName("Salvador Sakho"));
        userDao.update(userForDelete);
        }

    @Test
    public void testDeleteUser(){
        UserDao userDao = new UserDao(new SessionFactoryClass("mysql").getSession()); /// mysql - will be as parameter
        List <User> userForDelete = new ArrayList<User>();

        /* Users Examples */
        User user = new User().setFullName("aPriori Default User").setRawLoginID("adu");
        User salvaUser = new User().setFullName("Salvador Sakho").setRawLoginID("ssakho");

        userForDelete.add(user);
        userForDelete.add(salvaUser);
        userDao.delete(userDao.getAllObjects(userForDelete.get(0).getClass()));
    }

    @Test
    public void testGetUserByNameFromDB(){
        UserDao userDao = new UserDao(new SessionFactoryClass("mysql").getSession()); /// mysql - will be as parameter
        User user = new User().setFullName("aPriori Default User");
        System.out.println(userDao.getByFullName(user).getUser_ID());
    }
}
