package com.itberries2018.demo.auth.daointerfaces;

import com.itberries2018.demo.auth.entities.User;

import java.util.List;

public interface UserDao {

    User add(String userName, String email, String password, String avatar);


    List<User> getAll();

    User getById(Long id);


    void updateUser(User user, Long id);

    boolean isUserExist(User user);

    User findByEmail(String email);

    class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(String email, Throwable cause) {
            super("User with email " + email + " already exists", cause);
        }
    }
}
