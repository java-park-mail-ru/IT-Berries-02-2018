package com.itberries2018.demo.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name="history")
public class History {
    @Id
    @Column(name = "id_history")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_history;

    @Column(name="score")
    private Integer score;

    @Column(name="user_id")
    private Long user_id;

    @Column(name="date_result")
    private Date date_result;

    public History(){

    }

    public Long getId_history() {
        return id_history;
    }

    public void setId_history(Long id_history) {
        this.id_history = id_history;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Date getDate_result() {
        return date_result;
    }

    public void setDate_result(Date date_result) {
        this.date_result = date_result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(id_history, history.id_history) &&
                Objects.equals(score, history.score) &&
                Objects.equals(user_id, history.user_id) &&
                Objects.equals(date_result, history.date_result);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id_history, score, user_id, date_result);
    }

    @Override
    public String toString() {
        return "History{" +
                "id_history=" + id_history +
                ", score=" + score +
                ", user_id=" + user_id +
                ", date_result='" + date_result + '\'' +
                '}';
    }
}

