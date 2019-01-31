package br.com.jhonicosta.instagram_clone.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.jhonicosta.instagram_clone.helper.ConfiguracaoFirebase;

public class Comentario implements Serializable {

    private String idComentario, idPostagem, idUsuario, caminhoFoto, nomeUsuario, comentario;

    public Comentario() {
    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(String idPostagem) {
        this.idPostagem = idPostagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public boolean salvar() {

        DatabaseReference comentariosRef = ConfiguracaoFirebase.getFirebase()
                .child("comentarios")
                .child(getIdPostagem());
        String chaveComentario = comentariosRef.push().getKey();
        setIdComentario(chaveComentario);
        comentariosRef.child(getIdComentario()).setValue(this);

        return true;
    }
}
