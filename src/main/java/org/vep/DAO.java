package org.vep;

import org.vep.models.Patient;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by mw on 11/8/15.
 */
public class DAO {
    Logger logger;

    public DAO() {
        logger = Logger.getLogger("org.vep.DAO");
        entityManagerFactory = Persistence.createEntityManagerFactory("vep");
    }

    public void bye() {
        entityManagerFactory.close();
    }

    public List<Patient> getPatients() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        logger.info("entered tx");
        List<Patient> patients = em.createQuery("from Patient", Patient.class).getResultList();
        logger.info("found " + patients.size() + " patients");
        em.getTransaction().commit();
        em.close();
        logger.info("exited tx");
        return patients;
    }

    private EntityManagerFactory entityManagerFactory;
}
