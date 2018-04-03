package com.itberries2018.demo.servicesImpl;

import com.itberries2018.demo.Entities.History;
import com.itberries2018.demo.daoInterfaces.HistoryDao;
import com.itberries2018.demo.daoInterfaces.UserDao;
import com.itberries2018.demo.Entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.sql.Date;
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
    public History add(String date_result, int score, User user) {
        History history = new History();
        history.setScore(score);
        history.setUser_id(user);
        history.setDate_result(convertStringTodate(date_result));

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

    public Date convertStringTodate(String date){//"2011-02-10"
        Date parsed = Date.valueOf(date);
        return  parsed;
    }

    @Override
    public List<History> getAll() {
        return em.createQuery("select * from history").getResultList();
    }

    @Override
    public List<Object[]> getSortedData() {
        String query = "select   hist, pl from History as hist, User as pl where hist.user = pl.id";
        List<Object[]>results = em.createQuery(query).getResultList();
        return results;
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
