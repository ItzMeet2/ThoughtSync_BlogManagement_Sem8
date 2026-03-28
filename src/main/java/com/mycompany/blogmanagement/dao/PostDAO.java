package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.Post;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PostDAO {
    public void save(Post post) {
        JPAUtil.executeInTransaction(em -> em.persist(post));
    }

    public Post findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Post.class, id);
        } finally {
            em.close();
        }
    }

    public List<Post> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p ORDER BY p.createdAt DESC", Post.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Post> findPublished() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p WHERE p.status = :status ORDER BY p.publishedAt DESC", Post.class)
                    .setParameter("status", "published")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Post> findByAuthor(Integer authorId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p WHERE p.author.userId = :authorId ORDER BY p.createdAt DESC", Post.class)
                    .setParameter("authorId", authorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Post> findByCategory(Integer categoryId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p WHERE p.category.categoryId = :categoryId AND p.status = :status ORDER BY p.publishedAt DESC", Post.class)
                    .setParameter("categoryId", categoryId)
                    .setParameter("status", "published")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void update(Post post) {
        JPAUtil.executeInTransaction(em -> em.merge(post));
    }

    public void delete(Integer id) {
        JPAUtil.executeInTransaction(em -> {
            Post post = em.find(Post.class, id);
            if (post != null) {
                em.remove(post);
            }
        });
    }

    public List<Post> searchPosts(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p WHERE (LOWER(p.title) LIKE LOWER(:keyword) OR LOWER(p.content) LIKE LOWER(:keyword)) AND p.status = :status ORDER BY p.publishedAt DESC", Post.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setParameter("status", "published")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Post> findMostViewed(int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p WHERE p.status = :status ORDER BY p.viewCount DESC", Post.class)
                    .setParameter("status", "published")
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Post> findMostLiked(int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p WHERE p.status = :status ORDER BY p.likeCount DESC", Post.class)
                    .setParameter("status", "published")
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void incrementViewCount(Integer postId) {
        JPAUtil.executeInTransaction(em ->
            em.createQuery("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.postId = :postId")
                    .setParameter("postId", postId)
                    .executeUpdate()
        );
    }

    public Long getTotalPosts() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Post p", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public Long getPublishedCount() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Post p WHERE p.status = :status", Long.class)
                    .setParameter("status", "published")
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
