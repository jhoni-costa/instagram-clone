package br.com.jhonicosta.instagram_clone.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String id, nome, email, senha, urlFoto;

    public Usuario() {
    }

    public Usuario(String id, String nome, String email, String senha, String urlFoto) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.urlFoto = urlFoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}
