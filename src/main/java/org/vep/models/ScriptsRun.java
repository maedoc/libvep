package org.vep.models;

import javax.persistence.*;

/**
 * Created by mw on 11/8/15.
 *
 * Model for running the SCRIPTS pipeline.
 *
 * TODO map input/output paths to model classes
 * TODO setup class hierarchy so can modularize pipeline
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ScriptsRun {
    public ScriptsRun() {
    }

    @Id
    @GeneratedValue private long id;
    @ManyToOne private DWI dwi;
    @ManyToOne private T1 t1;
    private String workPath; // PRD
    private String subjId; // SUBJ_ID
    private String fslPrefix; // FSL
    private String mcrPath; // MCR
    private boolean visualChecks; // CHECK
    private int percentValueMask; // percent_value_mask;
    private int maxHarmonicOrder; // lmax
    private double regionMappingCorrection; // region_mapping_corr

    // TODO int[] in jpa? for now, space separated like in bash pipeline
    private String connectivitySubdivisions; // K_list

    private int numberOfTracks; // number_tracks
    private int parcellationDivision; // K
    private String parcellationName; // parcel

    public enum TopopCorrection { No, Reversed, EddyCorrect }
    @Enumerated(EnumType.STRING) private TopopCorrection topopCorrection; // topup

    private boolean useACT; // act
    private boolean useSIFT; // sift

    // other parameters relevant on cluster, see vibes/scripts/setup-env.sh
    private String fsSubjectsDir; // SUBJECTS_DIR
    private String fsHome; // FREESURFER_HOME
    private String mneRoot; // MNE_ROOT
    private String fslDir; // FSLDIR
    private String extraPaths; // export PATH=$PATH:$HOME/mrtrix3/bin:$HOME/mrtrix3/scripts:$FSLDIR/bin:$FSLDIR/scripts
    private String extraLdLibraryPaths; // export LD_LIBRARY_PATH=/home/duke/local/lib:$LD_LIBRARY_PATH

    // output files TODO extract to results object, relation with other model classes
    private String outputConnectivityZipPath;
    private String outputRegionMappingPath;
    private String outputCorticalSurfaceZipPath;
    private String outputBEMSurfacesZipPath;
    // etc


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DWI getDwi() {
        return dwi;
    }

    public void setDwi(DWI dwi) {
        this.dwi = dwi;
    }

    public T1 getT1() {
        return t1;
    }

    public void setT1(T1 t1) {
        this.t1 = t1;
    }

    public String getWorkPath() {
        return workPath;
    }

    public void setWorkPath(String workPath) {
        this.workPath = workPath;
    }

    public String getSubjId() {
        return subjId;
    }

    public void setSubjId(String subjId) {
        this.subjId = subjId;
    }

    public String getFslPrefix() {
        return fslPrefix;
    }

    public void setFslPrefix(String fslPrefix) {
        this.fslPrefix = fslPrefix;
    }

    public String getMcrPath() {
        return mcrPath;
    }

    public void setMcrPath(String mcrPath) {
        this.mcrPath = mcrPath;
    }

    public boolean isVisualChecks() {
        return visualChecks;
    }

    public void setVisualChecks(boolean visualChecks) {
        this.visualChecks = visualChecks;
    }

    public int getPercentValueMask() {
        return percentValueMask;
    }

    public void setPercentValueMask(int percentValueMask) {
        this.percentValueMask = percentValueMask;
    }

    public int getMaxHarmonicOrder() {
        return maxHarmonicOrder;
    }

    public void setMaxHarmonicOrder(int maxHarmonicOrder) {
        this.maxHarmonicOrder = maxHarmonicOrder;
    }

    public double getRegionMappingCorrection() {
        return regionMappingCorrection;
    }

    public void setRegionMappingCorrection(double regionMappingCorrection) {
        this.regionMappingCorrection = regionMappingCorrection;
    }

    public String getConnectivitySubdivisions() {
        return connectivitySubdivisions;
    }

    public void setConnectivitySubdivisions(String connectivitySubdivisions) {
        this.connectivitySubdivisions = connectivitySubdivisions;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public int getParcellationDivision() {
        return parcellationDivision;
    }

    public void setParcellationDivision(int parcellationDivision) {
        this.parcellationDivision = parcellationDivision;
    }

    public String getParcellationName() {
        return parcellationName;
    }

    public void setParcellationName(String parcellationName) {
        this.parcellationName = parcellationName;
    }

    public TopopCorrection getTopopCorrection() {
        return topopCorrection;
    }

    public void setTopopCorrection(TopopCorrection topopCorrection) {
        this.topopCorrection = topopCorrection;
    }

    public boolean isUseACT() {
        return useACT;
    }

    public void setUseACT(boolean useACT) {
        this.useACT = useACT;
    }

    public boolean isUseSIFT() {
        return useSIFT;
    }

    public void setUseSIFT(boolean useSIFT) {
        this.useSIFT = useSIFT;
    }

    public String getFsSubjectsDir() {
        return fsSubjectsDir;
    }

    public void setFsSubjectsDir(String fsSubjectsDir) {
        this.fsSubjectsDir = fsSubjectsDir;
    }

    public String getFsHome() {
        return fsHome;
    }

    public void setFsHome(String fsHome) {
        this.fsHome = fsHome;
    }

    public String getMneRoot() {
        return mneRoot;
    }

    public void setMneRoot(String mneRoot) {
        this.mneRoot = mneRoot;
    }

    public String getFslDir() {
        return fslDir;
    }

    public void setFslDir(String fslDir) {
        this.fslDir = fslDir;
    }

    public String getExtraPaths() {
        return extraPaths;
    }

    public void setExtraPaths(String extraPaths) {
        this.extraPaths = extraPaths;
    }

    public String getExtraLdLibraryPaths() {
        return extraLdLibraryPaths;
    }

    public void setExtraLdLibraryPaths(String extraLdLibraryPaths) {
        this.extraLdLibraryPaths = extraLdLibraryPaths;
    }

    public String getOutputConnectivityZipPath() {
        return outputConnectivityZipPath;
    }

    public void setOutputConnectivityZipPath(String outputConnectivityZipPath) {
        this.outputConnectivityZipPath = outputConnectivityZipPath;
    }

    public String getOutputRegionMappingPath() {
        return outputRegionMappingPath;
    }

    public void setOutputRegionMappingPath(String outputRegionMappingPath) {
        this.outputRegionMappingPath = outputRegionMappingPath;
    }

    public String getOutputCorticalSurfaceZipPath() {
        return outputCorticalSurfaceZipPath;
    }

    public void setOutputCorticalSurfaceZipPath(String outputCorticalSurfaceZipPath) {
        this.outputCorticalSurfaceZipPath = outputCorticalSurfaceZipPath;
    }

    public String getOutputBEMSurfacesZipPath() {
        return outputBEMSurfacesZipPath;
    }

    public void setOutputBEMSurfacesZipPath(String outputBEMSurfacesZipPath) {
        this.outputBEMSurfacesZipPath = outputBEMSurfacesZipPath;
    }
}
