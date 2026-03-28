package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.Category;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CategoryDAO {
    public void save(Category category) {
        JPAUtil.executeInTransaction(em -> em.persist(category));
    }

    public Category findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public List<Category> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c ORDER BY c.categoryName", Category.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void update(Category category) {
        JPAUtil.executeInTransaction(em -> em.merge(category));
    }

    public void delete(Integer id) {
        JPAUtil.executeInTransaction(em -> {
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
            }
        });
    }

    public Long getTotalCategories() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Category c", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}
