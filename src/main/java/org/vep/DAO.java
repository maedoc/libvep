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
                logger.info("created emf through generic interface");
            } catch (PersistenceException e) {
                PersistenceProvider hibernate = new org.hibernate.jpa.HibernatePersistenceProvider();
                entityManagerFactory = hibernate.createEntityManagerFactory("vep", null);
                logger.warning("created emf through hibernate provider");
                if (Util.runningInMATLAB()) {
                    logger.info("MATLAB detected: consider add vep jar to javaclasspath.txt");
                }
            }
        } else {
            logger.info("using existing entity manager factory");
        }
        entityManager = entityManagerFactory.createEntityManager();
        logger.info("created entity manager");
    }

    public void close() {
        entityManager.close();
    }

    public void closeEMF() {
        entityManagerFactory.close();
        entityManagerFactory = null;
    }

    public List<Patient> getPatients() {
        entityManager.getTransaction().begin();
        logger.info("entered tx");
        List<Patient> patients = entityManager
                .createQuery("from Patient", Patient.class).getResultList();
        logger.info("found " + patients.size() + " patients");
        entityManager.getTransaction().commit();
        logger.info("exited tx");
        return patients;
    }

    public long addPatient(Patient patient) {
        entityManager.getTransaction().begin();
        logger.info("adding patient");
        entityManager.persist(patient);
        logger.info("added patient id=" + patient.getId());
        entityManager.getTransaction().commit();
        return patient.getId();
    }

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
}
