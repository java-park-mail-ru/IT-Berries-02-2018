package com.itberries2018.demo.daointerfaces;

import com.itberries2018.demo.entities.User;

import java.util.List;

public interface UserDao {

    User add(String userName, String email, String password, String avatar);


    List<User> getAll();

    User getById(Long id);


    void updatePassword(Long id, String newPassword);


    void updateAvatar(Long id, String avatar);

    void updateUser(User user);


    void remove(User id);

    boolean isUserExist(User user);

    User findByEmail(String email);

    class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(String email, Throwable cause) {
            super("User with email " + email + " already exists", cause);
        }
    }
}
