import daoImpl.UserDao;
import entity.User;
import org.junit.Test;
import utils.SessionFactoryClass;

public class UserTest {

    @Test
    public void testGetUserFromDB(){
        UserDao userDao = new UserDao(new SessionFactoryClass("mysql").getSession()); /// mysql - will be as parameter
        System.out.println(userDao.get(new User().setFullName("Salvador Sakho")));
    }


    @Test
    public void testCreateUser(){

    }

    @Test
    public void testUpdateUser(){

    }

    @Test
    public void testDeleteUser(){

    }

    @Test
    public void testCreateDefaultUser(){

    }


}
