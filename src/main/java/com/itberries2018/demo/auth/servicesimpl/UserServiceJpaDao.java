package com.itberries2018.demo.auth.servicesimpl;

import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.auth.daointerfaces.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;

import java.util.List;

import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;

@Transactional
@Repository
public class UserServiceJpaDao implements UserDao {
    @PersistenceContext
    private final EntityManager em;

    public UserServiceJpaDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public User add(String userName, String email, String password, String avatar) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(email);
        user.setAvatar(avatar);
        try {
            em.persist(user);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolation) {
                throw new DuplicateUserException(userName, ex);
            } else {
                throw ex;
            }

        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return em.createQuery("select c from User c ", User.class).getResultList();
    }

    @Override
    public User getById(Long id) {
        return em.find(User.class, id);
    }


    @Override
    public void updateUser(User user, Long id) {
        Query query = em.createQuery("UPDATE User u SET u.password=:password, u.email=:email,"
                + " u.avatar=:avatar , u.username=:username  where u.id =:id")
                .setParameter("email", user.getEmail())
                .setParameter("password", user.getPassword())
                .setParameter("avatar", user.getAvatar())
                .setParameter("username", user.getUsername())
                .setParameter("id", id);
        query.executeUpdate();

    }

    @Override
    public boolean isUserExist(User user) {
        Query que = em.createQuery("SELECT c FROM User c where c.username=:user_name", User.class);
        que.setParameter("user_name", user.getUsername());
        List<User> lst = que.getResultList();
        System.out.println(lst.toString());
        return lst.size() > 0;
    }

    @Override
    public User findByEmail(String email) {
        Query que = em.createQuery("SELECT c FROM User c where c.email=:user_email", User.class);
        que.setParameter("user_email", email);
        List<User> lst = que.getResultList();
        System.out.println(lst.toString());
        if (lst.size() > 0) {
            return lst.get(0);
        } else {
            return null;
        }
    }
}
