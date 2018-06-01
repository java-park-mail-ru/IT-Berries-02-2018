package com.itberries2018.demo.auth.servicesimpl;


import com.itberries2018.demo.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itberries2018.demo.auth.servicesintefaces.ScoreRecordService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public boolean incrementScore(Long id, int score) {

        User userSave = this.userServiceJpaDao.getById(id);
        try {
            this.historyServiceJpaDao.add(Timestamp.valueOf(LocalDateTime.now()).toString(), score, userSave);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public int getBestScoreForUserById(Long id) {
        return this.historyServiceJpaDao.getBestScoreForUserById(id);
    }
}
