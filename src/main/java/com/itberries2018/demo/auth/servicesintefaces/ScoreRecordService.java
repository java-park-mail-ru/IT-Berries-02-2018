package com.itberries2018.demo.auth.servicesintefaces;

import com.itberries2018.demo.auth.models.ScoreRecord;

import java.util.List;

public interface ScoreRecordService {

    List<ScoreRecord> converUsersToSocreRecords(List<Object[]> users);

    void incrementScore(Long id);

}
