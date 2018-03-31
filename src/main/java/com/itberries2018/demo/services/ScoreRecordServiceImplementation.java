package com.itberries2018.demo.services;

import com.itberries2018.demo.models.ScoreRecord;
import com.itberries2018.demo.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreRecordServiceImplementation implements ScoreRecordService {

    @Override
    public List<ScoreRecord> converUsersToSocreRecords(List<User> users) {
        final List<ScoreRecord> records = new ArrayList<>();
        for (User user : users) {
            records.add(new ScoreRecord(user));
        }
        return records;
    }
}
