package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class JdbcUserDaoTest extends TenmoDaoTests{

    private static final User USER_1 = new User(1111L, "testUser1", "password", "USER");
    private static final User USER_2 = new User(2222L, "testUser2", "password", "USER");
    private static final User USER_3 = new User(3333L, "testUser3", "password", "USER");
    private static final User USER_4 = new User(4444L, "testUser4", "password", "USER");
    private static final User USER_5 = new User(5555L, "testUser5", "password", "USER");

    private JdbcUserDao sut;


    private User testUser;

    @Before
    public void setup() {
        sut = new JdbcUserDao(dataSource);
        testUser = new User(9999L, "testUser9", "password", "USER");
    }

    @Test
    public void findByUsername_input_user_name_returns_id() {
        User userId = sut.findByUsername("testUser1");
        Assert.assertNotNull("findByUserName returned null", userId);
        assertUsersMatch("User information returned does not match.", USER_1, userId);

        userId = sut.findByUsername("testUser5");
        assertUsersMatch("User information returned does not match.", USER_5, userId);
    }

    @Test
    public void findAll() {
        List<User> users = sut.findAll();
        Assert.assertNotNull("Null return for list of users");
        Assert.assertEquals("List return is not correct size",sut.findAll().size(), users.size());
    }

    @Test
    public void findIdByUsername() {
        int userId = sut.findIdByUsername("testUser3");
        Long longId = (long) userId;
        Assert.assertEquals(USER_3.getId(), longId);

        int userId2 = sut.findIdByUsername("testuser3");
        Long longId2 = (long) userId2;
        Assert.assertEquals("case insensitive username does not return correct id",USER_3.getId(), longId2);

    }

    @Test
    public void create() {
        boolean userCreated = sut.create("XxquesoxX", "noscope");
        Assert.assertTrue("Did not create a new user", userCreated);

        boolean userNotCreated = sut.create("testUser2", "password");
        Assert.assertFalse("Create duplicates user",userNotCreated);
    }

    private void assertUsersMatch(String message, User expected, User actual) {

        Assert.assertEquals(message, expected.getId(), actual.getId());
        Assert.assertEquals(message, expected.getUsername(), actual.getUsername());
        Assert.assertEquals(message, expected.isActivated(), actual.isActivated());
    }
}