package br.com.jhonicosta.instagram_clone.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;

import br.com.jhonicosta.instagram_clone.helper.ConfiguracaoFirebase;

public class PostagemCurtida implements Serializable {

    private Feed feed;
    private Usuario usuario;
    private int qtdCurtidas = 0;

    public PostagemCurtida() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getQtdCurtidas() {
        return qtdCurtidas;
    }

    public void setQtdCurtidas(int qtdCurtidas) {
        this.qtdCurtidas = qtdCurtidas;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public void salvar() {
        HashMap<String, Object> dadosUsuario = new HashMap<>();
        dadosUsuario.put("nomeUsuario", usuario.getNome());
        dadosUsuario.put("caminhoFoto", usuario.getCaminhoFoto());

        DatabaseReference curtidasRef = ConfiguracaoFirebase.getFirebase()
                .child("postagens-curtidas")
                .child(feed.getId())
                .child(usuario.getId());

        curtidasRef.setValue(dadosUsuario);

        atualizarQuandidade(1);

    }

    public void atualizarQuandidade(int qtd) {
        DatabaseReference curtidasRef = ConfiguracaoFirebase.getFirebase()
                .child("postagens-curtidas")
                .child(feed.getId())
                .child("qtdCurtidas");
        setQtdCurtidas(getQtdCurtidas() + qtd);
        curtidasRef.setValue(getQtdCurtidas());
    }

    public void remover() {
        DatabaseReference curtidasRef = ConfiguracaoFirebase.getFirebase()
                .child("postagens-curtidas")
                .child(feed.getId())
                .child(usuario.getId());
        curtidasRef.removeValue();

        atualizarQuandidade(-1);
    }
}
