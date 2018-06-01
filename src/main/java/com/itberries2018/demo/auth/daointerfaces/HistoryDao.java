package com.itberries2018.demo.auth.daointerfaces;

import com.itberries2018.demo.auth.entities.History;
import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.auth.models.ScoreRecord;

import java.util.List;

public interface HistoryDao {

    History add(String dateResult, int score, User user);

    int getBestScoreForUserById(Long id);

    List<ScoreRecord> getSortedData();


}
