package org.vep.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mw on 11/8/15.
 *
 * A model for running Stan
 */

@Entity
public class StanModel {

    public class RDataInput { }

    protected StanModel() { }

    public StanModel(String modelCode, String rDataPath, String csvOutputPath) {
        this.modelCode = modelCode;
        this.rDataPath = rDataPath;
        this.csvOutputPath = csvOutputPath;
    }

    @Id
    @GeneratedValue
    private long id;
    private Date startDate;
    private Date finishDate;
    private String cmdStanVersion;
    private String modelCode;
    private String rDataPath;
    private String csvOutputPath;

    public String getCachedCompiledModelPath() {
        return cachedCompiledModelPath;
    }

    public void setCachedCompiledModelPath(String cachedCompiledModelPath) {
        this.cachedCompiledModelPath = cachedCompiledModelPath;
    }

    private String cachedCompiledModelPath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getCmdStanVersion() {
        return cmdStanVersion;
    }

    public static class CmdStanVersion {
        public final int major, minor, point;

        public CmdStanVersion(int major, int minor, int point) {
            this.major = major;
            this.minor = minor;
            this.point = point;
        }

        /* TODO not a robust implementation! */
        public CmdStanVersion(String versionString) {
            this(2, 8, 0);
        }

        @Override
        public String toString() {
            return "CmdStan " + major + "." + minor + "." + point;
        }
    }

    public void setCmdStanVersion(String cmdStanVersion) {
        // TODO contrived example of what we'd want to do..
        CmdStanVersion ver = new CmdStanVersion(cmdStanVersion);
        if (!(ver.major >= 2 && ver.minor >= 7))
            System.err.println("must have CmdStan 2.7 or greater, have " + ver);
        this.cmdStanVersion = cmdStanVersion;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getrDataPath() {
        return rDataPath;
    }

    public void setrDataPath(String rDataPath) {
        this.rDataPath = rDataPath;
    }

    public String getCsvOutputPath() {
        return csvOutputPath;
    }

    public void setCsvOutputPath(String csvOutputPath) {
        this.csvOutputPath = csvOutputPath;
    }
}
