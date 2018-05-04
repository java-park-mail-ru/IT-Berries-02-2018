package com.itberries2018.demo.servicesimpl;

import com.itberries2018.demo.entities.History;
import com.itberries2018.demo.models.ScoreRecord;
import com.itberries2018.demo.entities.User;
import org.springframework.stereotype.Service;
import com.itberries2018.demo.servicesintefaces.ScoreRecordService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreRecordServiceImplementation implements ScoreRecordService {

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
    public void incrementScore(Long id) {

    }
}
