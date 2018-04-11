package com.itberries2018.demo.servicesintefaces;

import com.itberries2018.demo.entities.User;
import com.itberries2018.demo.models.ScoreRecord;

import java.util.List;

public interface UserService {

    boolean isUserExist(User user);

    List<User> makeScoreboardPage();

    User findByEmail(String email);

    User findById(long id);

    User findByLogin(String name);

    void saveUser(User user);

    void saveHistoryNote(String dateResult, int score, User user);

    void updateUser(User user);

    List<User> findAllUsers();

    List<ScoreRecord> findAllUsersForScoreBoard();

}
