package com.mycompany.blogmanagement.dao;

import com.mycompany.blogmanagement.entity.Report;
import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ReportDAO {

    public void save(Report report) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(report);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void update(Report report) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(report);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Report findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Report.class, id);
        } finally {
            em.close();
        }
    }

    public List<Report> findPendingReports() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Report r LEFT JOIN FETCH r.post LEFT JOIN FETCH r.reporter WHERE r.status = 'PENDING' ORDER BY r.createdAt DESC", Report.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }
    
    public int getPendingReportCount() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(r) FROM Report r WHERE r.status = 'PENDING'", Long.class)
                           .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Report report = em.find(Report.class, id);
            if (report != null) {
                em.remove(report);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
