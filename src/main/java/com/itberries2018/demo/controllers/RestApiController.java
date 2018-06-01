package com.itberries2018.demo.controllers;

import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.auth.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.itberries2018.demo.auth.servicesintefaces.ScoreRecordService;
import com.itberries2018.demo.auth.servicesintefaces.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"https://itberries-frontend.herokuapp.com", "http://localhost:8080",
        "http://localhost:8081", "http://localhost", "http://it-berries.neat.codes",
        "it-berries.neat.codes/:1", "http://it-berries.neat.codes/",
        "https://it-berries.neat.codes", "https://it-berries.neat.codes/", "*"},
        allowCredentials = "true", allowedHeaders = {"origin", "content-type", "accept", "authorization"})
public class RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class);

    private final UserService userService;

    private final ScoreRecordService scoreRecordService;


    private PasswordEncoder passwordEncoder;

    @Autowired
    public RestApiController(UserService userService, ScoreRecordService scoreRecordService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.scoreRecordService = scoreRecordService;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(MultipartHttpServletRequest request, HttpServletResponse response,
                                   HttpSession httpSession) {
        LOGGER.info("Trying to login user");


        final LoginForm loginForm = new LoginForm(request.getParameter("email"), request.getParameter("password"));

        final User user = userService.findByEmail(loginForm.getEmail());
        if (user == null || !passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
            LOGGER.error("Unable to login. User with email {} not found.", loginForm.getEmail());
            return new ResponseEntity<>(Map.ofEntries(entry("error", "Не верно указан E-Mail или пароль")),
                    HttpStatus.BAD_REQUEST);
        }
        user.setPassword("");
        httpSession.setAttribute("user", user);

        int score = this.scoreRecordService.getBestScoreForUserById(user.getId());
        Map<String, Object> information = new HashMap<>();
        information.put("username", user.getUsername());
        information.put("email", user.getEmail());
        information.put("avatar", user.getAvatar());
        information.put("score", score);

        return new ResponseEntity<>(information, HttpStatus.OK);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> registration(MultipartHttpServletRequest request, HttpSession httpSession, HttpServletResponse response) {

        final String login = request.getParameter("username");
        final String email = request.getParameter("email");
        final String password = passwordEncoder.encode(request.getParameter("password"));
        final String repPassword = request.getParameter("password_repeat");
        final String avatar = request.getParameter("avatar");

        if (login == null) {
            return new ResponseEntity<>(new ErrorJson("Specify a correct login!"), HttpStatus.BAD_REQUEST);
        } else {
            if (email == null || !email.matches("(.*)@(.*)")) {
                return new ResponseEntity<>(new ErrorJson("Specify a valid e-mail!"), HttpStatus.BAD_REQUEST);
            } else {
                if (password == null || password.length() < 4) {
                    return new ResponseEntity<>(new ErrorJson("The password field must contain more than "
                            + "4 characters!"), HttpStatus.BAD_REQUEST);
                } else {
                    if (repPassword == null || !passwordEncoder.matches(repPassword, password)) {
                        return new ResponseEntity<>(new ErrorJson("Repeat password correctly!"), HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }

        if (userService.findByEmail(email) != null) {
            return new ResponseEntity<>(new ErrorJson("User already exists!"), HttpStatus.CONFLICT);
        }

        final String avatarName;
        if (avatar == null || avatar.equals("")) {
            avatarName = "noavatar.png";
        } else {
            avatarName = avatar;
        }

        final User user = new User();
        user.setAvatar(avatarName);
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(login);
        userService.saveUser(user);

        final User userCurrent = userService.findByEmail(email);
        userCurrent.setPassword("");
        httpSession.setAttribute("user", userCurrent);

        int score = this.scoreRecordService.getBestScoreForUserById(userCurrent.getId());
        Map<String, Object> information = new HashMap<>();
        information.put("username", userCurrent.getUsername());
        information.put("email", userCurrent.getEmail());
        information.put("avatar", userCurrent.getAvatar());
        information.put("score", score);

        return new ResponseEntity<>(information, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity<?> authentication(HttpServletResponse response, HttpSession httpSession) {
        LOGGER.info("Trying to authentificate user");

        final User currentUser = (User) httpSession.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("The user isn't authorized",
                    HttpStatus.UNAUTHORIZED);
        }
        int score = this.scoreRecordService.getBestScoreForUserById(currentUser.getId());
        Map<String, Object> information = new HashMap<>();
        information.put("username", currentUser.getUsername());
        information.put("email", currentUser.getEmail());
        information.put("avatar", currentUser.getAvatar());
        information.put("score", score);
        return new ResponseEntity<>(information, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/scoreboard", method = RequestMethod.GET)
    public ResponseEntity<?> scoreboard(@RequestParam("listSize") String ssize,
                                        @RequestParam("listNumber") String spage, HttpServletResponse response) {

        final int page = Integer.parseInt(spage);
        final int size = Integer.parseInt(ssize);


        if (page < 1) {
            return new ResponseEntity<>(new ErrorJson("This sheet may not be formed!"), HttpStatus.BAD_REQUEST);
        }
        final int startPosition = (page - 1) * size;
        List<ScoreRecord> results = userService.findAllUsersForScoreBoard();

        if (results.size() < startPosition + size) {
            return new ResponseEntity<>(Map.ofEntries(entry("scorelist", results.subList(startPosition, results.size())),
                    entry("length", results.size())), HttpStatus.OK);
        }

        return new ResponseEntity<>(Map.ofEntries(entry("scorelist", results.subList(startPosition, startPosition + size)),
                entry("length", userService.findAllUsersForScoreBoard().size())), HttpStatus.OK);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logOut(HttpServletResponse response, HttpSession httpSession) {
        httpSession.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/me", method = RequestMethod.POST)
    public ResponseEntity<?> meProfile(MultipartHttpServletRequest request, HttpSession httpSession, HttpServletResponse response) {

        final User currentUser = (User) httpSession.getAttribute("user");


        if (currentUser == null) {
            LOGGER.error("Unable to auth.");
            return new ResponseEntity<>("The user isn't authorized",
                    HttpStatus.UNAUTHORIZED);
        }

        final String newLogin = request.getParameter("username");
        final String newEmail = request.getParameter("email");
        final String password = request.getParameter("current_password");
        final String newPassword = passwordEncoder.encode(request.getParameter("new_password"));
        final String newPasswordRepeat = request.getParameter("new_password_repeat");
        final MultipartFile avatar = request.getFile("avatar");

        String oldPassword = userService.findByEmail(currentUser.getEmail()).getPassword();
        if (!passwordEncoder.matches(password, oldPassword)) {
            return new ResponseEntity<>(new ErrorJson("Wrong password"), HttpStatus.BAD_REQUEST);
        }

        if (newEmail != null && !newEmail.equals(currentUser.getEmail())) {
            if (!newEmail.matches("(.*)@(.*)")) {
                LOGGER.error("Not valid email");
                return new ResponseEntity<>(new ErrorJson("Not valid email"), HttpStatus.BAD_REQUEST);
            }
            if (userService.findByEmail(newEmail) != null) {
                LOGGER.error("User already exists!");
                return new ResponseEntity<>(new ErrorJson("User already exists!"), HttpStatus.CONFLICT);
            }
            currentUser.setEmail(newEmail);
        }
        if (newPassword != null) {
            if (newPassword.length() < 4) {
                LOGGER.error("New password must be longer than 3 characters");
                return new ResponseEntity<>(new ErrorJson("New password must be longer than 3 characters"), HttpStatus.BAD_REQUEST);
            }
            if (!passwordEncoder.matches(newPasswordRepeat, newPassword)) {
                LOGGER.error("New passwords do not match");
                return new ResponseEntity<>(new ErrorJson("New passwords do not match"), HttpStatus.BAD_REQUEST);
            }
            currentUser.setPassword(newPassword);
        }
        if (newLogin != null) {
            currentUser.setUsername(newLogin);
        } else {
            return new ResponseEntity<>(new ErrorJson("Login is required!"), HttpStatus.BAD_REQUEST);
        }

        if (avatar != null && !avatar.equals("")) {
            currentUser.setAvatar(currentUser.getUsername() + "_avatar");
            try {

                File newAvater = new File("/home/cloud/front/2018_1_IT-Berries/public/avatars/"
                        + currentUser.getUsername() + "_avatar");
                newAvater.createNewFile();
                avatar.transferTo(newAvater);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return new ResponseEntity<>(new ErrorJson("Avatar is required!"), HttpStatus.BAD_REQUEST);
        }

        Long id = currentUser.getId();
        userService.updateUser(currentUser, id);
        httpSession.setAttribute("user", currentUser);

        currentUser.setPassword("");
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}

