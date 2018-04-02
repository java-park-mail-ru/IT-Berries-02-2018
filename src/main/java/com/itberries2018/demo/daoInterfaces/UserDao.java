package com.itberries2018.demo.daoInterfaces;

import com.itberries2018.demo.models.User;

import java.util.List;

public interface UserDao {
    //create
    User add(String userName, String email, String password, String avatar);

    //read
    List<User> getAll();

    User getById(Long id);

    //update Password
    void updatePassword(Long id, String newPassword);

    //update Password
    void updateAvatar(Long id, String avatar);

    //delete
    void remove(User id);

    boolean isUserExist(User user);

    class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(String email, Throwable cause) {
            super("User with email " + email + " already exists", cause);
        }
    }
}
