package org.javadominicano.dto;

public class ResumenEstacionesDTO {
    private int total;
    private int activas;
    private int inactivas;
    private String primeraInactiva;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getActivas() {
        return activas;
    }

    public void setActivas(int activas) {
        this.activas = activas;
    }

    public int getInactivas() {
        return inactivas;
    }

    public void setInactivas(int inactivas) {
        this.inactivas = inactivas;
    }

    public String getPrimeraInactiva() {
        return primeraInactiva;
    }

    public void setPrimeraInactiva(String primeraInactiva) {
        this.primeraInactiva = primeraInactiva;
    }
}
