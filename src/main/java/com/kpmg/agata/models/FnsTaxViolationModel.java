package com.kpmg.agata.models;

import java.util.Objects;

public class FnsTaxViolationModel extends FnsModel {

    private String fine;

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FnsTaxViolationModel that = (FnsTaxViolationModel) o;
        return Objects.equals(fine, that.fine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fine);
    }
}
