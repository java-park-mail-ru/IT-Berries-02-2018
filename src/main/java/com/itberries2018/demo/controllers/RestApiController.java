package com.itberries2018.demo.controllers;

import com.itberries2018.demo.models.LoginForm;
import com.itberries2018.demo.models.User;
import com.itberries2018.demo.services.CustomErrorType;
import com.itberries2018.demo.services.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;


@CrossOrigin(origins = {"https://itberries-frontend.herokuapp.com", "http://localhost:9000"})
@RestController
@RequestMapping("/api")
public class RestApiController {
    @SuppressWarnings("WeakerAccess")
    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class);

    private final UserService userService; //Service which will do all data retrieval/manipulation work

    @Autowired
    public RestApiController(UserService userService) {
        this.userService = userService;
    }

    // -------------------Retrieve All Users---------------------------------------------

    @RequestMapping(value = "/users/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        final List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // -------------------Retrieve Single User------------------------------------------

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("id") long id) {
        LOGGER.info("Fetching User with id {}", id);
        final User user = userService.findById(id);
        if (user == null) {
            LOGGER.error("User with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // -------------------Create a User-------------------------------------------

    @RequestMapping(value = "/signUp/", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user, HttpSession httpSession, UriComponentsBuilder ucBuilder) {

        LOGGER.info("Creating User : {}", user);

        if (userService.isUserExist(user)) {
            LOGGER.error("Unable to create. A User with name {} already exist", user.getName());
            return new ResponseEntity<>(new CustomErrorType(), HttpStatus.CONFLICT);
        }
        userService.saveUser(user);

        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    // ------------------- Update a User ------------------------------------------------

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        LOGGER.info("Updating User with id {}", id);

        final User currentUser = userService.findById(id);

        if (currentUser == null) {
            LOGGER.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType(),
                    HttpStatus.NOT_FOUND);
        }

        currentUser.setLogin(user.getLogin());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());


        userService.updateUser(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }


    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm, HttpServletResponse response,
                                   HttpSession httpSession) {
        LOGGER.info("Trying to login user");

        final User user = userService.findByLogin(loginForm.getLogin());
        if (user == null || !user.getPassword().equals(loginForm.getPassword())) {
            LOGGER.error("Unable to delete. User with id {} not found.");
            return new ResponseEntity<>(new CustomErrorType(),
                    HttpStatus.NOT_FOUND);
        }
        httpSession.setAttribute("user", user);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


    @RequestMapping(value = "/logOut/", method = RequestMethod.POST)
    public String logOut(HttpServletResponse response, HttpSession httpSession) {

        httpSession.invalidate();
        return "redirect:/login/";
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        LOGGER.info("Trying to logOut user");
//
//        if (userId == null) {
//            LOGGER.info("Already out");
//            return new ResponseEntity<>(new CustomErrorType(),
//                    HttpStatus.ALREADY_REPORTED);
//        }
//
//        httpSession.setAttribute("online", false);
//
//        LOGGER.info("Clearing cookie");
//        final Cookie cookie = new Cookie("userId", null);
//        cookie.setPath("/api/");
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//
        //  return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/currentUser/", method = RequestMethod.POST)
    public ResponseEntity<?> currentUser(HttpSession httpSessione) {

        final Enumeration<String> names = httpSessione.getAttributeNames();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            if (name.equals("online")) {
                if (httpSessione.getAttribute(name).equals(true)) {
                    final Long id = (long) httpSessione.getAttribute("Id");
                    final User user = userService.findById(id);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
                return new ResponseEntity<>(new CustomErrorType(),
                        HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(new CustomErrorType(),
                HttpStatus.NOT_FOUND);
    }

}

