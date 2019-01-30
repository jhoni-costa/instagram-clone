package br.com.jhonicosta.instagram_clone.model;

import java.io.Serializable;

public class PostagemCurtida implements Serializable {

    private int qtdCurtidas = 0;

    public PostagemCurtida() {
    }

    public int getQtdCurtidas() {
        return qtdCurtidas;
    }

    public void setQtdCurtidas(int qtdCurtidas) {
        this.qtdCurtidas = qtdCurtidas;
    }
}
