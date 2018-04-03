package com.itberries2018.demo.servicesIntefaces;

import com.itberries2018.demo.Entities.History;
import com.itberries2018.demo.Entities.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.List;

public interface UserService {
    User findById(long id);

    User findByLogin(String name);

    User findByEmail(String email);

    void saveUser(User user);

    void saveHistoryNote(String dateResult, int score, User user);

    void updateUser(User user);

    List<User> findAllUsers();

    List<Object[]> findAllUsersForScoreBoard();

    List<User> makeScoreboardPage();

    boolean isUserExist(User user);

    //User makeUser(String login, String email, String password, String avatarName);
}
