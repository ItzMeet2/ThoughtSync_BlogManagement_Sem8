package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.Comment;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CommentDAO {
    public void save(Comment comment) {
        JPAUtil.executeInTransaction(em -> em.persist(comment));
    }

    public Comment findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Comment.class, id);
        } finally {
            em.close();
        }
    }

    public List<Comment> findByPost(Integer postId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Comment c WHERE c.post.postId = :postId AND c.status = 'approved' ORDER BY c.createdAt DESC", Comment.class)
                    .setParameter("postId", postId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Comment> findPending() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Comment c WHERE c.status = 'pending' ORDER BY c.createdAt DESC", Comment.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void update(Comment comment) {
        JPAUtil.executeInTransaction(em -> em.merge(comment));
    }

    public void delete(Integer id) {
        JPAUtil.executeInTransaction(em -> {
            Comment comment = em.find(Comment.class, id);
            if (comment != null) {
                em.remove(comment);
            }
        });
    }
}
