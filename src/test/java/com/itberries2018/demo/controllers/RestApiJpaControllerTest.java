package com.itberries2018.demo.controllers;

import com.itberries2018.demo.daointerfaces.UserDao;
import com.itberries2018.demo.servicesintefaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class RestApiJpaControllerTest extends RestApiControllerTest {
    @Autowired
    private UserService dao;

    @Override
    protected UserService getDao() {
        return dao;
    }


}