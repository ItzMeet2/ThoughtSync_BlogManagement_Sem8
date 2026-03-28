package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.Tag;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class TagDAO {

    public void save(Tag tag) {
        JPAUtil.executeInTransaction(em -> em.persist(tag));
    }

    public Tag findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Tag.class, id);
        } finally {
            em.close();
        }
    }

    public Tag findByName(String name) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tag t WHERE t.tagName = :name", Tag.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Tag> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tag t ORDER BY t.tagName", Tag.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        JPAUtil.executeInTransaction(em -> {
            Tag tag = em.find(Tag.class, id);
            if (tag != null) em.remove(tag);
        });
    }
}
