package org.vep;

import org.vep.models.Patient;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by mw on 11/8/15.
 */
public class DAO {
    Logger logger;

    public DAO() {
        logger = Logger.getLogger("org.vep.DAO");
        if (entityManagerFactory==null) {
            logger.info("creating entity manager factory");
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory("vep");
            } catch (PersistenceException e) {
                PersistenceProvider hibernate = new org.hibernate.jpa.HibernatePersistenceProvider();
                entityManagerFactory = hibernate.createEntityManagerFactory("vep", null);
            }
        } else {
            logger.info("using existing entity manager factory");
        }
        logger.info("creating entity manager");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void close() {
        entityManager.close();
    }

    public List<Patient> getPatients() {
        entityManager.getTransaction().begin();
        logger.info("entered tx");
        List<Patient> patients = entityManager
                .createQuery("from Patient", Patient.class).getResultList();
        logger.info("found " + patients.size() + " patients");
        entityManager.getTransaction().commit();
        entityManager.close();
        logger.info("exited tx");
        return patients;
    }

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
}
