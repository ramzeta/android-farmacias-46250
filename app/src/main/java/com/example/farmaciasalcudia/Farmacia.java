package com.example.farmaciasalcudia;
public class Farmacia {
    private String date;
    private String guardia;
    private String reforc;
    private String guardiaLocation;
    private String reforcLocation;

    public Farmacia(String date, String guardia, String reforc) {
        this.date = date;
        this.guardia = guardia;
        this.reforc = reforc;
    }

    public String getDate() {
        return date;
    }

    public String getGuardia() {
        return guardia;
    }

    public String getReforc() {
        return reforc;
    }

    public String getGuardiaLocation() {
        return guardiaLocation;
    }

    public void setGuardiaLocation(String guardiaLocation) {
        this.guardiaLocation = guardiaLocation;
    }

    public String getReforcLocation() {
        return reforcLocation;
    }

    public void setReforcLocation(String reforcLocation) {
        this.reforcLocation = reforcLocation;
    }

    @Override
    public String toString() {
        return "Farmacia{" +
                "date='" + date + '\'' +
                ", guardia='" + guardia + '\'' +
                ", reforc='" + reforc + '\'' +
                ", guardiaLocation='" + guardiaLocation + '\'' +
                ", reforcLocation='" + reforcLocation + '\'' +
                '}';
    }
}