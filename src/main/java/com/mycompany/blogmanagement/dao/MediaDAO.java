package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.Media;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class MediaDAO {

    public void save(Media media) {
        JPAUtil.executeInTransaction(em -> em.persist(media));
    }

    public Media findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Media.class, id);
        } finally {
            em.close();
        }
    }

    public List<Media> findByUser(Integer userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Media m WHERE m.uploadedBy.userId = :userId ORDER BY m.createdAt DESC", Media.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Media> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Media m ORDER BY m.createdAt DESC", Media.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        JPAUtil.executeInTransaction(em -> {
            Media media = em.find(Media.class, id);
            if (media != null) em.remove(media);
        });
    }
}
