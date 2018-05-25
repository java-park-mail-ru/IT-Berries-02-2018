package com.itberries2018.demo.auth.daointerfaces;

import com.itberries2018.demo.auth.entities.History;
import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.auth.servicesimpl.UserServiceJpaDao;
import com.itberries2018.demo.auth.servicesintefaces.ScoreRecordService;
import com.itberries2018.demo.auth.servicesintefaces.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserDaoTest {


    @Mock
    History history1, history2;

    @Autowired
    protected UserService userService;

    @Autowired
    ScoreRecordService scoreRecordService;

    @Autowired
    private UserServiceJpaDao userDao;


    @Test
    void add() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        User newUser = userDao.add(user.getUsername(), user.getEmail(), user.getPassword(), user.getAvatar());
        assertEquals("username", newUser.getUsername());
        assertEquals("password", newUser.getPassword());
        assertEquals("avatar.png", newUser.getAvatar());
        assertEquals("user@mail.ru", newUser.getEmail());
    }

    @Test
    void updateUser() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        user.setPassword("newPassword");
        user.setId(7);
        userService.updateUser(user, Long.valueOf(7));
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void findByEmail() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        userDao.add(user.getUsername(), user.getEmail(), user.getPassword(), user.getAvatar());
        User newUser = userDao.findByEmail("user@mail.ru");
        assertEquals(newUser.getUsername(), user.getUsername());
    }


    @Test
    public void findByIdTrue() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        User newUser = userDao.add(user.getUsername(), user.getEmail(), user.getPassword(), user.getAvatar());
        long expected = newUser.getId();
        User user1 = userService.findById(expected);
        Assert.assertThat(expected, is(user1.getId()));
    }

    @Test
    public void findByIdFalse() {
        User userExpected = userService.findById(4);
        Assert.assertEquals(userExpected, null);
    }

    @Test
    public void userExistsTrue() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        User newUser = userDao.add(user.getUsername(), user.getEmail(), user.getPassword(), user.getAvatar());
        boolean expected = userService.isUserExist(newUser);
        Assert.assertEquals(expected, true);
    }


    @Test
    public void incrementScoreTest() {
        User newUser = userDao.add("username", "user@mail.ru", "password", "avatar.png");
        boolean expected = scoreRecordService.incrementScore(newUser.getId(), 500);
        Assert.assertEquals(expected, true);
    }
}