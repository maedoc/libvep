package org.vep.models;

import java.util.Date;
import javax.persistence.*;

/**
 * Created by mw on 11/7/15.
 *
 * Any kind of raw or original data measured on a patient is an exam.
 *
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Exam {

    @Id @GeneratedValue private long id;
    @Enumerated(EnumType.STRING) private ExamType type;
    private Date date;
    private String dataPath;
    @ManyToOne private Patient patient;

    protected Exam() { }

    public Exam(Patient patient, ExamType type, Date date) {
        this.patient = patient;
        this.type = type;
        this.date = date;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ExamType getType() {
        return type;
    }

    public void setType(ExamType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
