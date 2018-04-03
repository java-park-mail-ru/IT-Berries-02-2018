package com.itberries2018.demo.servicesImpl;

import com.itberries2018.demo.Entities.User;
import com.itberries2018.demo.daoInterfaces.UserDao;
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
        try{
            em.persist(user);
        }catch (PersistenceException ex){
            if(ex.getCause() instanceof ConstraintViolation){
                throw new DuplicateUserException(userName, ex);
            }else{
                throw ex;
            }

        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return em.createQuery("select c from User c ",User.class).getResultList();
    }

    @Override
    public User getById(Long id) {
        return em.find(User.class,id);
    }

    @Override
    public void updatePassword(Long user_id, String newPassword) {
        em.createQuery("UPDATE users SET users.password=:newPassword " +
                "WHERE users.user_id=:user_id", User.class).setParameter("newPassword", newPassword).setParameter("user_id",user_id);
    }

    @Override
    public void updateAvatar(Long user_id, String avatar) {
        em.createQuery("UPDATE users SET users.avatar=:avatar WHERE users.user_is=:user_id", User.class).
                setParameter("avatar",avatar).setParameter("user_id", user_id);
    }

    @Override
    public void updateUser(User user) {
       Query query = em.createQuery("UPDATE User u SET u.password=:password, u.email=:email, u.avatar=:avatar where u.username=:username")
                .setParameter("email", user.getEmail())
                .setParameter("password",user.getPassword())
                .setParameter("avatar", user.getAvatar())
                .setParameter("username", user.getUsername());
       query.executeUpdate();

    }

    @Override
    public void remove(User user) {
        remove(user);
    }

    @Override
    public boolean isUserExist(User user) {// Check if the instance is a managed entity instance belonging to the current persistence context
        Query q = em.createQuery("SELECT c FROM User c where c.username=:user_name", User.class);
        q.setParameter("user_name", user.getUsername());
        List<User> lst = q.getResultList();
        System.out.println(lst.toString());
        if(lst.size()>0)
            return  true;
        else
            return false;
    }

    @Override
    public User findByEmail(String email) {
        Query q = em.createQuery("SELECT c FROM User c where c.email=:user_email", User.class);
        q.setParameter("user_email", email);
        List<User> lst = q.getResultList();
        System.out.println(lst.toString());
        if(lst.size()>0)
            return  lst.get(0);
        else
            return null;
    }
}
