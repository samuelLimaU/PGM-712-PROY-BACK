package com.multimedia.spring.multimed.aplicacion.dto;

import java.util.List;

public class AprioriRuleDTO {
    private List<String> antecedente;
    private List<String> consecuente;
    private double soporte;
    private double confianza;
    private double lift;

    public AprioriRuleDTO() {}

    public AprioriRuleDTO(List<String> antecedente, List<String> consecuente, double soporte, double confianza, double lift) {
        this.antecedente = antecedente;
        this.consecuente = consecuente;
        this.soporte = soporte;
        this.confianza = confianza;
        this.lift = lift;
    }

    // Getters y Setters
    public List<String> getAntecedente() { return antecedente; }
    public void setAntecedente(List<String> antecedente) { this.antecedente = antecedente; }

    public List<String> getConsecuente() { return consecuente; }
    public void setConsecuente(List<String> consecuente) { this.consecuente = consecuente; }

    public double getSoporte() { return soporte; }
    public void setSoporte(double soporte) { this.soporte = soporte; }

    public double getConfianza() { return confianza; }
    public void setConfianza(double confianza) { this.confianza = confianza; }

    public double getLift() { return lift; }
    public void setLift(double lift) { this.lift = lift; }
}
