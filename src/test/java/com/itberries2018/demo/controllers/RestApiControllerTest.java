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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class RestApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void registration() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/registration")
                        .header("content-type", MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("username", "testUserName1")
                        .param("password", "testUserName1")
                        .param("password_repeat", "testUserName1")
                        .param("avatar", "avatar1.png")
                        .param("email", "testUserName1@mail.ru");
        mockMvc.perform(builder).andExpect(status().is(HttpStatus.CREATED.value()));


        mockMvc.perform(builder).andExpect(status().is(HttpStatus.CONFLICT.value()));


        builder = MockMvcRequestBuilders.post("/registration")
                .header("content-type", MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("password", "testUserName1")
                .param("password_repeat", "testUserName1")
                .param("avatar", "avatar1.png")
                .param("email", "testUserName1@mail.ru");
        mockMvc.perform(builder).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));


    }

    @Test
    void login() throws Exception {
        registration();
        MockHttpSession mockSession = new MockHttpSession();
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/registration")
                        .session(mockSession)
                        .header("content-type", MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("password", "testUserName")
                        .param("email", "testUserName@mail.ru");
        mockMvc.perform(builder).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }


    @Test
    void authentication() throws Exception {

        MockHttpSession mocksession = new MockHttpSession();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/me")
                        .session(mocksession);
        mockMvc.perform(builder).andExpect(status().isUnauthorized());

    }

    @Test
    void scoreboard() throws Exception {
        Timestamp testDateResult = new Timestamp(System.currentTimeMillis());
        int score = 99;
        User testUser = new User("testUserName", "testUserEmail@mail.ru", "testPassword", "testAvatar.png");
        userService.saveHistoryNote(testDateResult.toString(), score, testUser);


        List<ScoreRecord> results = userService.findAllUsersForScoreBoard();

        assertNotNull(results);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/users/scoreboard")
                        .param("listSize", "1")
                        .param("listNumber", "1");

        mockMvc.perform(builder).andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void logOut() throws Exception {
        login();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.delete("/logout").content("");
        this.mockMvc.perform(builder).andExpect(status().is(HttpStatus.OK.value()));
    }

}