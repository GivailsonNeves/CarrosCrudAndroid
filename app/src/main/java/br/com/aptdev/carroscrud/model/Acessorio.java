package br.com.aptdev.carroscrud.model;

import java.io.Serializable;

/**
 * Created by gneves on 27/06/2017.
 */

public class Acessorio implements Serializable {
    Long id;
    String nomeAcessorio;
    Boolean opcional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeAcessorio() {
        return nomeAcessorio;
    }

    public void setNomeAcessorio(String nomeAcessorio) {
        this.nomeAcessorio = nomeAcessorio;
    }

    public Boolean getOpcional() {
        return opcional;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    @Override
    public String toString() {
        return getNomeAcessorio() + " - " + (getOpcional() ? "Acessório opcional" : "Acessório de fábrica");
    }
}
