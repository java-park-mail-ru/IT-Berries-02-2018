package com.itberries2018.demo.daointerfaces;

import com.itberries2018.demo.entities.User;
import com.itberries2018.demo.servicesintefaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
abstract class UserDaoTest {

    protected abstract UserService getDao();

    @Test
    void add() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        getDao().saveUser(user);
        assertNotNull(user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("avatar.png", user.getAvatar());
        assertEquals("user@mail.ru", user.getEmail());

    }

    @Test
    void updateUser() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        user.setPassword("newPassword");
        getDao().updateUser(user);
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void findByEmail() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        User newUser = getDao().findByEmail("user@mail.ru");
        assertEquals(newUser.getId(), user.getId());
    }
}