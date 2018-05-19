package com.itberries2018.demo.auth.daointerfaces;

import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.auth.servicesintefaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserDaoTest {

    protected  UserService userService;

    @Test
    void add() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        userService.saveUser(user);
        assertNotNull(user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("avatar.png", user.getAvatar());
        assertEquals("user@mail.ru", user.getEmail());

    }

    @Test
    void updateUser() {
//        User user = new User("username", "user@mail.ru", "password", "avatar.png");
//        user.setPassword("newPassword");
//        //userService.updateUser(user, id);
//        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void findByEmail() {
        User user = new User("username", "user@mail.ru", "password", "avatar.png");
        User newUser = userService.findByEmail("user@mail.ru");
        assertEquals(newUser.getId(), user.getId());
    }
}