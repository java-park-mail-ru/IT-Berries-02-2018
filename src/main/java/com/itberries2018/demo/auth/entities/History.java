package com.itberries2018.demo.auth.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "history")
public class History {
    @Id
    @Column(name = "id_history")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistory;

    @Column(name = "score")
    private Integer score;


    @ManyToOne(targetEntity = User.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date_result")
    private Timestamp dateResult;

    public History() {

    }


    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }


    public void setDate_result(Timestamp dateResultNew) {
        this.dateResult = dateResultNew;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        History history = (History) obj;
        return Objects.equals(idHistory, history.idHistory)
                && Objects.equals(score, history.score)
                && Objects.equals(user, history.user)
                && Objects.equals(dateResult, history.dateResult);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idHistory, score, user, dateResult);
    }

    public void setUser_id(User newUser) {
        this.user = newUser;
    }
}

