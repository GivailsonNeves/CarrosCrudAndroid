package br.com.aptdev.carroscrud.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gneves on 27/06/2017.
 */

public class Carro implements Serializable{
    Long id;
    String nomeModelo;
    Integer anoModelo;
    Integer aroRoda;
    List<Acessorio> acessorios;

    public List<Acessorio> getAcessorios() {
        return acessorios;
    }

    public void setAcessorios(List<Acessorio> acessorios) {
        this.acessorios = acessorios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeModelo() {
        return nomeModelo;
    }

    public void setNomeModelo(String nomeModelo) {
        this.nomeModelo = nomeModelo;
    }

    public Integer getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(Integer anoModelo) {
        this.anoModelo = anoModelo;
    }

    public Integer getAroRoda() {
        return aroRoda;
    }

    public void setAroRoda(Integer aroRoda) {
        this.aroRoda = aroRoda;
    }

    @Override
    public String toString() {
        return getId()+ ": " + getNomeModelo() +" - "+ getAnoModelo();
    }
}
