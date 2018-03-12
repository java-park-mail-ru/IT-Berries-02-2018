package com.itberries2018.demo.services;

import com.itberries2018.demo.models.User;
import com.itberries2018.demo.models.ScoreRecord;

import java.util.List;

public interface ScoreRecordService {

    List<ScoreRecord> converUsersToSocreRecords(List<User> users);

}
