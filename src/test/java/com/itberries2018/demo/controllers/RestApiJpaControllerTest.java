package com.itberries2018.demo.controllers;

import com.itberries2018.demo.auth.servicesintefaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;

class RestApiJpaControllerTest extends RestApiControllerTest {
    @Autowired
    private UserService dao;

    @Override
    protected UserService getDao() {
        return dao;
    }


}