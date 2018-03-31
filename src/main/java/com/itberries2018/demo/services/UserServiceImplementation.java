package com.itberries2018.demo.services;

import com.itberries2018.demo.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImplementation implements UserService {

    private static final AtomicLong COUNTER = new AtomicLong();


    private  final List<User> users;

    {
        users = populateDummyUsers();
    }

    @Override
    public List<User> findAllUsers() {
        return users;
    }

    @Override
    public User findById(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByLogin(String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void saveUser(User user) {
        users.add(user);
    }

    @Override
    public void updateUser(User user) {
        final int index = users.indexOf(user);
        users.set(index, user);
    }

    @Override
    public boolean isUserExist(User user) {
        return findByLogin(user.getName()) != null;
    }

    @SuppressWarnings("MagicNumber")
    private  List<User> populateDummyUsers() {
        final List<User> usersData = new ArrayList<>();
        usersData.add(new User(COUNTER.incrementAndGet(), "user1", "user1@mail.ru", "user1", 10));
        usersData.add(new User(COUNTER.incrementAndGet(), "user2", "user2@mail.ru", "user2", 20));
        usersData.add(new User(COUNTER.incrementAndGet(), "user3", "user3@mail.ru", "user3", 50));
        usersData.add(new User(COUNTER.incrementAndGet(), "user4", "user14@mail.ru", "user4", 100));
        return usersData;
    }

    @Override
    public List<User> makeScoreboardPage() {
        return users;
    }

    public User makeUser(String login, String email, String password, String avatarName) {
        return new User(COUNTER.incrementAndGet(), login, email, password, avatarName, 1);
    }

    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
}
