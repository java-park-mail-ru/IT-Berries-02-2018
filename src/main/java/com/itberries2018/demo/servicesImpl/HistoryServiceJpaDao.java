package com.itberries2018.demo.servicesImpl;

import com.itberries2018.demo.models.History;
import com.itberries2018.demo.daoInterfaces.HistoryDao;
import com.itberries2018.demo.daoInterfaces.UserDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.sql.Date;
import java.util.List;

@Transactional
@Repository
public class HistoryServiceJpaDao implements HistoryDao {

    private final EntityManager em;

    public HistoryServiceJpaDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public History add(Long id_history, Date date_result, int score, Long user_id) {
        History history = new History();
        history.setDate_result(date_result);
        history.setScore(score);
        history.setId_history(id_history);
        history.setUser_id(user_id);

        try {
            em.persist(history);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolation) {
                throw new UserDao.DuplicateUserException(id_history.toString(), ex);
            } else {
                throw ex;
            }

        }
        return history;
    }

    @Override
    public List<History> getAll() {
        return em.createQuery("select * from history").getResultList();
    }

    @Override
    public List<History> getSortedData() {
        return em.createQuery("select user_name, max(score) as score from history\n" +
                "join users using(user_id)\n" +
                "GROUP BY user_name\n" +
                "ORDER BY  score DESC").getResultList();
    }

    @Override
    public int getTheBestScoreForTheUser(Long user_id) {
        return em.createQuery("select max(score) from history where history.user_id=:user_id",
                History.class).setParameter("user_id", user_id).getFirstResult();
    }


    @Override
    public void remove(History history) {
        remove(history);
    }
}
