package com.itberries2018.demo.auth.daointerfaces;

import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.auth.servicesimpl.UserServiceJpaDao;
import com.itberries2018.demo.auth.servicesintefaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserDaoTest {

    @Autowired
    protected UserService userService;

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
}