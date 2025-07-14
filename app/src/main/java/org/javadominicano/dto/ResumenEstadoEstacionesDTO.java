package org.javadominicano.dto;

public class ResumenEstadoEstacionesDTO {
    private long total;
    private long activas;
    private long inactivas;
    private String primeraInactivaId;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getActivas() {
        return activas;
    }

    public void setActivas(long activas) {
        this.activas = activas;
    }

    public long getInactivas() {
        return inactivas;
    }

    public void setInactivas(long inactivas) {
        this.inactivas = inactivas;
    }

    public String getPrimeraInactivaId() {
        return primeraInactivaId;
    }

    public void setPrimeraInactivaId(String primeraInactivaId) {
        this.primeraInactivaId = primeraInactivaId;
    }
}