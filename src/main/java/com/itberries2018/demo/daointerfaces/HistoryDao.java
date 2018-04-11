package com.itberries2018.demo.daointerfaces;

import com.itberries2018.demo.entities.History;
import com.itberries2018.demo.entities.User;
import com.itberries2018.demo.models.ScoreRecord;

import java.util.List;

public interface HistoryDao {

    History add(String dateResult, int score, User user);


    List<ScoreRecord>  getSortedData();

    class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(String idHistory, Throwable cause) {
            super("History note with id: " + idHistory + " already exists", cause);
        }
    }
}
