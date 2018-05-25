package com.itberries2018.demo.auth.servicesimpl;

import com.itberries2018.demo.auth.entities.History;
import com.itberries2018.demo.auth.models.ScoreRecord;
import com.itberries2018.demo.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itberries2018.demo.auth.servicesintefaces.ScoreRecordService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public List<ScoreRecord> converUsersToSocreRecords(List<Object[]> results) {
        final List<ScoreRecord> records = new ArrayList<>();
        for (Object[] hiAndpl : results) {
            History entityHist = (History) hiAndpl[0];
            User entityUser = (User) hiAndpl[1];
            records.add(new ScoreRecord(entityUser, entityHist));
        }
        records.sort((o1, o2) -> Long.compare(o2.getScore(), o1.getScore()));
        for (int i = 0; i < records.size(); ++i) {
            records.get(i).setId(i);
        }
        return records;
    }

    @Override
    public void incrementScore(Long id, int score) {
        User userSave = this.userServiceJpaDao.getById(id);
        final User user = new User();
        user.setAvatar(userSave.getAvatar());
        user.setEmail(userSave.getEmail());
        user.setPassword(userSave.getPassword());
        user.setUsername(userSave.getUsername());
        this.historyServiceJpaDao.add(Timestamp.valueOf(LocalDateTime.now()).toString(), score, user);
    }
}
