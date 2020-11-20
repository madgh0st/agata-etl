package com.kpmg.agata.models;

import java.util.Objects;

public abstract class FnsModel extends AbstractClientDataModel {

    private String docId;
    private String docDate;
    private String creationDate;

    private String orgName;
    private String innul;

    public String getDocId() {
        return docId;
    }

    public String getDocDate() {
        return docDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getInnul() {
        return innul;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setInnul(String innul) {
        this.innul = innul;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FnsModel fnsModel = (FnsModel) o;
        return Objects.equals(docId, fnsModel.docId) &&
                Objects.equals(docDate, fnsModel.docDate) &&
                Objects.equals(creationDate, fnsModel.creationDate) &&
                Objects.equals(orgName, fnsModel.orgName) &&
                Objects.equals(innul, fnsModel.innul);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), docId, docDate, creationDate, orgName, innul);
    }
}
