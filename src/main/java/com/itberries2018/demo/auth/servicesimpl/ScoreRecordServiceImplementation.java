package com.itberries2018.demo.auth.servicesimpl;


import com.itberries2018.demo.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itberries2018.demo.auth.servicesintefaces.ScoreRecordService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class ScoreRecordServiceImplementation implements ScoreRecordService {

    private final HistoryServiceJpaDao historyServiceJpaDao;
    private final UserServiceJpaDao userServiceJpaDao;

    @Autowired
    public ScoreRecordServiceImplementation(HistoryServiceJpaDao historyServiceJpaDao,
                                            UserServiceJpaDao userServiceJpaDao) {
        this.historyServiceJpaDao = historyServiceJpaDao;
        this.userServiceJpaDao = userServiceJpaDao;
    }


    @Override
    public boolean incrementScore(Long id, int score) {
        User userSave = this.userServiceJpaDao.getById(id);
        final User user = new User();
        user.setAvatar(userSave.getAvatar());
        user.setEmail(userSave.getEmail());
        user.setPassword(userSave.getPassword());
        user.setUsername(userSave.getUsername());
        try {
            this.historyServiceJpaDao.add(Timestamp.valueOf(LocalDateTime.now()).toString(), score, user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public int getBestScoreForUserById(Long id) {
        return this.historyServiceJpaDao.getBestScoreForUserById(id);
    }
}
