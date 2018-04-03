package com.itberries2018.demo.servicesintefaces;

import com.itberries2018.demo.models.ScoreRecord;

import java.util.List;

public interface ScoreRecordService {

    List<ScoreRecord> converUsersToSocreRecords(List<Object[]> users);

}
