package com.itberries2018.demo.services;

import com.itberries2018.demo.models.User;

import java.util.List;

public interface UserService {
    User findById(long id);


    User findByLogin(String name);

    void saveUser(User user);

    void updateUser(User user);

    List<User> findAllUsers();

    boolean isUserExist(User user);
}
