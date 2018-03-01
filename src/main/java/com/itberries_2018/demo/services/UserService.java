package com.itberries_2018.demo.services;

import com.itberries_2018.demo.models.User;

import java.util.List;

public interface UserService {
    User findById(long id);


    User findByLogin(String name);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUserById(long id);

    List<User> findAllUsers();

    void deleteAllUsers();

    boolean isUserExist(User user);
}
