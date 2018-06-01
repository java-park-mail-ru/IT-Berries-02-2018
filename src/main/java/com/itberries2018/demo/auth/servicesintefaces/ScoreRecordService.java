package com.itberries2018.demo.auth.servicesintefaces;


public interface ScoreRecordService {

    boolean incrementScore(Long id, int score);

    int getBestScoreForUserById(Long id);
}
