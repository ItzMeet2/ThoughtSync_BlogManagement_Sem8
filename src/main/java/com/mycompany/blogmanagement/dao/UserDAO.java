package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.User;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class UserDAO {
    public void save(User user) {
        JPAUtil.executeInTransaction(em -> em.persist(user));
    }

    public User findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public User findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public User findByUsernameOrEmail(String identifier) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :email", User.class)
                    .setParameter("identifier", identifier)
                    .setParameter("email", identifier)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public User findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<User> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u", User.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void update(User user) {
        JPAUtil.executeInTransaction(em -> em.merge(user));
    }

    public void delete(Integer id) {
        JPAUtil.executeInTransaction(em -> {
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
        });
    }

    public Long getTotalUsers() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}
