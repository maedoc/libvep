package org.vep;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.*;
import org.vep.models.Exam;
import org.vep.models.ExamType;
import org.vep.models.Patient;


public class HbmTest {
	private EntityManagerFactory entityManagerFactory;

	@Before public void setup() throws Exception {
		boolean haveConnection = true;
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("vep");
		} catch (Exception e) {
			haveConnection = false;
			Assume.assumeTrue("assume have database connection", haveConnection);
		}
	}

	@After public void cleanUp() throws Exception {
		entityManagerFactory.close();
	}

	@Test public void testBasicUsage() {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		em.persist(new Patient("duke", "woodman", new Date(87, 06, 03)));
		em.persist(new Patient("sophie", "chen", new Date(84, 03, 27)));
		em.getTransaction().commit();
		em.close();

		em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		List result = em.createQuery("from Patient", Patient.class).getResultList();
		for (Patient patient : (List<Patient>) result)
			System.out.println("vep last name: " + patient.getLastName() + ".");
		em.getTransaction().commit(); // why needed when just reading?
		em.close();
	}

	@Test public void testExam() {
		EntityManager em = entityManagerFactory.createEntityManager();
		Date now = new Date();
		em.getTransaction().begin();
        Patient mw = new Patient("m", "w", new Date());
        mw.getExams().add(new Exam(mw, ExamType.ECoG, now));
        mw.getExams().add(new Exam(mw, ExamType.fMRI, now));
        em.persist(mw);
		em.getTransaction().commit();
		em.close();

		em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		Patient patient = em.createQuery("from Patient", Patient.class).getSingleResult();
        Exam ecogExam = patient.getExams().get(0);
		Assert.assertTrue("exam is ECoG", ecogExam.getType()==ExamType.ECoG);
        em.getTransaction().commit();
        em.close();
	}

}
