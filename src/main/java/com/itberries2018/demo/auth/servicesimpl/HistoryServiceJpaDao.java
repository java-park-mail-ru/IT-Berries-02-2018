package com.itberries2018.demo.auth.servicesimpl;

import com.itberries2018.demo.auth.entities.History;
import com.itberries2018.demo.auth.daointerfaces.HistoryDao;
import com.itberries2018.demo.auth.daointerfaces.UserDao;
import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.auth.models.ScoreRecord;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class HistoryServiceJpaDao implements HistoryDao {
    @PersistenceContext
    private final EntityManager em;

    public HistoryServiceJpaDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public History add(String dateResult, int score, User user) {
        History history = new History();
        history.setScore(score);
        history.setUser_id(user);
        history.setDate_result(convertStringToTimestamp(dateResult));

        try {
            em.persist(history);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolation) {
                throw new UserDao.DuplicateUserException("date", ex);
            } else {
                throw ex;
            }

        }
        return history;
    }

    private Timestamp convertStringToTimestamp(String date) {
        return Timestamp.valueOf(date);
    }


    @Override
    public List<ScoreRecord> getSortedData() {

        String query = "select u.username, max(h.score) as score "
                + "from History h , User u where u.id = h.user group by u.username order by score desc";

        List<Object[]> results = (List<Object[]>) em.createQuery(query).getResultList();
        final List<ScoreRecord> records = new ArrayList<>();

        for (Object[] note : results) {
            records.add(new ScoreRecord(Long.parseLong(note[1].toString()), note[0].toString()));
        }

        return records;
    }

}
