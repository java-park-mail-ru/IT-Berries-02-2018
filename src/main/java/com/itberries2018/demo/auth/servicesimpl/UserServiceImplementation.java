package com.itberries2018.demo.auth.servicesimpl;

import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.auth.models.ScoreRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itberries2018.demo.auth.servicesintefaces.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImplementation implements UserService {

    private static final AtomicLong COUNTER = new AtomicLong();

    private final UserServiceJpaDao userServiceJpaDao;

    private final HistoryServiceJpaDao historyServiceJpaDao;

    private final List<User> users;

    {
        users = populateDummyUsers();
    }

    @Autowired
    public UserServiceImplementation(UserServiceJpaDao userServiceJpaDao, HistoryServiceJpaDao historyServiceJpaDao) {
        this.userServiceJpaDao = userServiceJpaDao;
        this.historyServiceJpaDao = historyServiceJpaDao;
    }


    @Override
    public List<User> findAllUsers() {
        return userServiceJpaDao.getAll();
    }

    @Override
    public List<ScoreRecord> findAllUsersForScoreBoard() {
        return historyServiceJpaDao.getSortedData();
    }

    @Override
    public User findById(long id) {
        User user = userServiceJpaDao.getById(id);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }


    @Override
    public User saveUser(User user) {
        user = userServiceJpaDao.add(user.getUsername(), user.getEmail(), user.getPassword(), user.getAvatar());
        return user;
    }

    @Override
    public void saveHistoryNote(String dateResult, int score, User user) {
        historyServiceJpaDao.add(dateResult, score, user);
    }

    @Override
    public void updateUser(User user, Long id) {
        userServiceJpaDao.updateUser(user, id);
    }

    @Override
    public boolean isUserExist(User user) {
        return userServiceJpaDao.isUserExist(user);
    }

    @SuppressWarnings("MagicNumber")
    private List<User> populateDummyUsers() {
        return null;
    }


    public User findByEmail(String email) {
        return userServiceJpaDao.findByEmail(email);
    }
}
