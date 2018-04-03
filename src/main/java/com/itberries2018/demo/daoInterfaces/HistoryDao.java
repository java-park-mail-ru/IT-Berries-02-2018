package com.itberries2018.demo.daoInterfaces;

import com.itberries2018.demo.Entities.History;
import com.itberries2018.demo.Entities.User;

import java.util.List;

public interface HistoryDao {
    //create
    History add(String date_result, int score, User user);

    //read
    List<History> getAll();

    //read sorted data
    List <Object[]> getSortedData();



    int getTheBestScoreForTheUser(Long user_id);

    //delete
    void remove(History history);

    class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(String id_history, Throwable cause) {
            super("History note with id: " + id_history + " already exists", cause);
        }
    }
}
