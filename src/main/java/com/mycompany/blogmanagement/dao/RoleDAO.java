package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.Role;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class RoleDAO {
    public Role findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Role.class, id);
        } finally {
            em.close();
        }
    }
    
    public Role findByName(String roleName) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
                    .setParameter("roleName", roleName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<Role> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Role r", Role.class).getResultList();
        } finally {
            em.close();
        }
    }
}
