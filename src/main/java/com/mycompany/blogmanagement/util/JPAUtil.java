package com.mycompany.blogmanagement.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.UserTransaction;
import javax.naming.InitialContext;

public class JPAUtil {
    private static EntityManagerFactory emf;

    public static synchronized void init() {
        if (emf == null || !emf.isOpen()) {
            try {
                System.out.println("Initializing EntityManagerFactory...");
                emf = Persistence.createEntityManagerFactory("BlogPU");
                System.out.println("EntityManagerFactory initialized successfully!");
            } catch (Exception e) {
                System.err.println("ERROR: Failed to create EntityManagerFactory");
                e.printStackTrace();
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public static EntityManager getEntityManager() {
        if (emf == null || !emf.isOpen()) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }
        return emf.createEntityManager();
    }

    /**
     * Look up the JTA UserTransaction from JNDI.
     * Use this to manage transactions in DAOs when performing write operations.
     */
    public static UserTransaction getUserTransaction() {
        try {
            return (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        } catch (Exception e) {
            throw new RuntimeException("Failed to look up UserTransaction", e);
        }
    }

    /**
     * Execute a write operation (persist/merge/remove) within a JTA transaction.
     * This handles begin/commit/rollback automatically.
     */
    public static void executeInTransaction(TransactionalOperation op) {
        UserTransaction utx = getUserTransaction();
        EntityManager em = getEntityManager();
        try {
            utx.begin();
            em.joinTransaction();
            op.execute(em);
            utx.commit();
        } catch (Exception e) {
            try { utx.rollback(); } catch (Exception re) { /* ignore */ }
            if (e instanceof RuntimeException) throw (RuntimeException) e;
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    @FunctionalInterface
    public interface TransactionalOperation {
        void execute(EntityManager em) throws Exception;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
