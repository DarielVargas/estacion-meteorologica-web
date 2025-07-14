package org.javadominicano.dto;

public class AlertaMensajeDTO {
    private Long id;
    private long fecha;
    private String texto;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public long getFecha() { return fecha; }
    public void setFecha(long fecha) { this.fecha = fecha; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}
