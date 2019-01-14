package helpers;

import javax.persistence.EntityManager;

public class TransactionsHandler {
    public static void beginTx(EntityManager entityManager) {
        if (!entityManager.getTransaction().isActive())
            entityManager.getTransaction().begin();
    }

    public static void commitTx(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive())
            entityManager.getTransaction().commit();
    }

    public static void rollbackTx(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive())
            entityManager.getTransaction().rollback();
    }
}

