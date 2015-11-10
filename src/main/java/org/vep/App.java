package org.vep;

import org.vep.models.Patient;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DAO dao = new DAO();
        for (Patient patient : dao.getPatients())
            System.out.println("patient " + patient.getLastName() +  ", "
                                          + patient.getFirstName());
        dao.close();
        System.out.println( "Hello World!" );
    }
}
