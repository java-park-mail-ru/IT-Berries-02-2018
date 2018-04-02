package com.itberries2018.demo.daoInterfaces;

import com.itberries2018.demo.models.History;

import java.sql.Date;
import java.util.List;

public interface HistoryDao {
    //create
    History add(Long id_history, Date date_result, int score, Long user_id);

    //read
    List<History> getAll();

    //read sorted data
    List<History> getSortedData();

    int getTheBestScoreForTheUser(Long user_id);

    //delete
    void remove(History history);

    class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(String id_history, Throwable cause) {
            super("History note with id: " + id_history + " already exists", cause);
        }
    }
}
