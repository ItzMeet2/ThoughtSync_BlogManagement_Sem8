package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.PostRating;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class PostRatingDAO {

    public void save(PostRating rating) {
        JPAUtil.executeInTransaction(em -> em.persist(rating));
    }

    public void update(PostRating rating) {
        JPAUtil.executeInTransaction(em -> em.merge(rating));
    }

    public PostRating findByPostAndUser(Integer postId, Integer userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT pr FROM PostRating pr WHERE pr.post.postId = :postId AND pr.user.userId = :userId", PostRating.class)
                    .setParameter("postId", postId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Double getAverageRating(Integer postId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT AVG(pr.rating) FROM PostRating pr WHERE pr.post.postId = :postId", Double.class)
                    .setParameter("postId", postId)
                    .getSingleResult();
        } catch (Exception e) {
            return 0.0;
        } finally {
            em.close();
        }
    }
}
