package com.itberries2018.demo.controllers;

import com.itberries2018.demo.models.*;
import com.itberries2018.demo.services.CustomErrorType;
import com.itberries2018.demo.services.ScoreRecordService;
import com.itberries2018.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api")
public class RestApiController {
    @SuppressWarnings("WeakerAccess")
    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class);

    private final UserService userService; //Service which will do all data retrieval/manipulation work
    private final ScoreRecordService scoreRecordService;

    @Autowired
    public RestApiController(UserService userService, ScoreRecordService scoreRecordService) {
        this.userService = userService;
        this.scoreRecordService = scoreRecordService;
    }

    // -------------------Check auth-----------------------------------------------------

    @RequestMapping(value = "/me/", method = RequestMethod.GET)
    public ResponseEntity<?> me(@CookieValue(value = "frontend", required = false) Cookie frontend) {
        int id = Integer.parseInt(frontend.getValue());
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
    public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
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


    @SuppressWarnings("MagicNumber")
    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public ResponseEntity<?> login(@CookieValue(value = "userId", required = false) Cookie userId,
                                   @RequestBody LoginForm loginForm, HttpServletResponse response,
                                   HttpSession httpSession) {

        LOGGER.info("Trying to login user");

        if (userId != null) {
            LOGGER.info("Already in");
            return new ResponseEntity<>(new CustomErrorType(),
                    HttpStatus.ALREADY_REPORTED);
        }

        final User user = userService.findByLogin(loginForm.getLogin());
        if (user == null || !user.getPassword().equals(loginForm.getPassword())) {
            LOGGER.error("Unable to delete. User with id {} not found.");
            return new ResponseEntity<>(new CustomErrorType(),
                    HttpStatus.NOT_FOUND);
        }

        httpSession.setAttribute("online", true);
        httpSession.setAttribute("Id", user.getId());

        LOGGER.info("Setting cookie");
        final Cookie cookie = new Cookie("userId", Objects.toString(user.getId()));
        cookie.setPath("/api/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


    @RequestMapping(value = "/logOut/", method = RequestMethod.POST)
    public ResponseEntity<?> logOut(@CookieValue(value = "userId", required = false) Cookie userId, HttpServletResponse response,
                                    HttpSession httpSession) {

        LOGGER.info("Trying to logOut user");

        if (userId == null) {
            LOGGER.info("Already out");
            return new ResponseEntity<>(new CustomErrorType(),
                    HttpStatus.ALREADY_REPORTED);
        }

        httpSession.setAttribute("online", false);

        LOGGER.info("Clearing cookie");
        final Cookie cookie = new Cookie("userId", null);
        cookie.setPath("/api/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return new ResponseEntity(HttpStatus.ACCEPTED);
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

    // ------------------- Get Scoreboard page ------------------------------------------------

    @RequestMapping(value = "/users/scoreboard", method = RequestMethod.GET)
    public ResponseEntity<?> scoreboard(@RequestParam("size") int size, @RequestParam("page") int page) {
        if (page < 1) {
            return new ResponseEntity<>(new ErrorJson("Данный лист не может быть сформирован"), HttpStatus.BAD_REQUEST);
        }
        int startPosition = (page - 1) * size;
        List<User> users = userService.findAllUsers();
        users.sort((o1, o2) -> o2.getScore().compareTo(o1.getScore()));
        if (users.size() < startPosition + size) {
            return new ResponseEntity<>(new ErrorJson("Данный лист не может быть сформирован"), HttpStatus.BAD_REQUEST);
        }
        users = users.subList(startPosition, startPosition + size);
        List<ScoreRecord> records = scoreRecordService.converUsersToSocreRecords(users);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // ------------------- Registrate a User ------------------------------------------------

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<?> registration(MultipartHttpServletRequest request) {

        String login = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String repPassword = request.getParameter("repeat_password");
        MultipartFile avatar = request.getFile("avatar");

        if (login == null || email == null || password == null || repPassword == null || password.length() < 4
                || !email.matches("(.*)@(.*)") || !password.equals(repPassword)) {
            return new ResponseEntity<>(new ErrorJson("Не валидные данные пользователя"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByEmail(email) != null) {
            return new ResponseEntity<>(new ErrorJson("Пользователь уже существует"), HttpStatus.CONFLICT);
        }

        String avatarName = (avatar == null) ? "noavatar.png" : avatar.getName();
        User user = userService.makeUser(login, email, password, avatarName);

        userService.saveUser(user);

        return new ResponseEntity<>(new SuccessJson("Пользователь успешно зарегестрирован"), HttpStatus.CREATED);
    }

}

