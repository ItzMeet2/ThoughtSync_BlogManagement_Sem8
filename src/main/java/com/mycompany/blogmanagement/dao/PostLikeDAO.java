package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.Post;
import com.mycompany.blogmanagement.entity.PostLike;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PostLikeDAO {

    public void save(PostLike like) {
        JPAUtil.executeInTransaction(em -> em.persist(like));
    }

    public PostLike findByPostAndUser(Integer postId, Integer userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT pl FROM PostLike pl WHERE pl.post.postId = :postId AND pl.user.userId = :userId", PostLike.class)
                    .setParameter("postId", postId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void delete(PostLike like) {
        JPAUtil.executeInTransaction(em ->
            em.remove(em.contains(like) ? like : em.merge(like))
        );
    }

    public Long countByPost(Integer postId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.postId = :postId", Long.class)
                    .setParameter("postId", postId)
                    .getSingleResult();
        } catch (Exception e) {
            return 0L;
        } finally {
            em.close();
        }
    }

    public List<Post> findLikedPostsByUser(Integer userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT pl.post FROM PostLike pl WHERE pl.user.userId = :userId ORDER BY pl.createdAt DESC", 
                Post.class
            )
            .setParameter("userId", userId)
            .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        } finally {
            em.close();
        }
    }
}
