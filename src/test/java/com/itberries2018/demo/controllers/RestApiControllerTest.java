package com.itberries2018.demo.controllers;

import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.auth.models.ScoreRecord;
import com.itberries2018.demo.auth.servicesintefaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
class RestApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void registration() throws Exception {

        //RESPONSE CREATED 201
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/registration")
                        .header("content-type", "multipart/form-data")
                        .param("username", "testUserName")
                        .param("password", "testUserName")
                        .param("password_repeat", "testUserName")
                        .param("avatar", "avatar.png")
                        .param("email", "testUserName@mail.ru");
        mockMvc.perform(builder).andExpect(status().is(201));

        //RESPONCE CONFLICT 409
        mockMvc.perform(builder).andExpect(status().is(409));


        //RESPONCE BAD_REQUEST 400
        builder = MockMvcRequestBuilders.post("/registration")
                .header("content-type", "multipart/form-data")
                .param("password", "testUserName")
                .param("password_repeat", "testUserName")
                .param("avatar", "avatar.png")
                .param("email", "testUserName@mail.ru");
        mockMvc.perform(builder).andExpect(status().is(400));


    }

    @Test
    void login() throws Exception {
        registration();
        MockHttpSession mockSession = new MockHttpSession();
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/registration")
                        .session(mockSession)
                        .header("content-type", "multipart/form-data")
                        .param("password", "testUserName")
                        .param("email", "testUserName@mail.ru");
        mockMvc.perform(builder).andExpect(status().is(400));
    }


    @Test
    void authentication() throws Exception {
        //RESPONCE Unauthorized
        MockHttpSession mocksession = new MockHttpSession();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/me")
                        .session(mocksession);
        mockMvc.perform(builder).andExpect(status().isUnauthorized());

    }

    @Test
    void scoreboard() throws Exception {
        //Test data for a history note
        String testDateResult = "2028-01-01";
        int score = 99;
        User testUser = new User("testUserName", "testUserEmail@mail.ru", "testPassword", "testAvatar.png");
        userService.saveHistoryNote(testDateResult, score, testUser);


        List<ScoreRecord> results = userService.findAllUsersForScoreBoard();

        assertNotNull(results);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/users/scoreboard")
                        .param("listSize", "1")
                        .param("listNumber", "1");

        mockMvc.perform(builder).andExpect(status().is(200));
    }

    @Test
    void logOut() throws Exception {
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.delete("/logout").content("");
        this.mockMvc.perform(builder).andExpect(status().is(200));
    }

    protected UserService getDao() {
        return userService;
    }
}