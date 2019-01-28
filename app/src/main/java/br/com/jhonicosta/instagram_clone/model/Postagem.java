package br.com.jhonicosta.instagram_clone.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.com.jhonicosta.instagram_clone.helper.ConfiguracaoFirebase;
import br.com.jhonicosta.instagram_clone.helper.UsuarioFirebase;

public class Postagem implements Serializable {

    private String id;
    private String idUsuario;
    private String descricao;
    private String caminhoFoto;

    public Postagem() {
        DatabaseReference reference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference postagemRef = reference.child("postagens");
        String idPostagem = postagemRef.push().getKey();
        setId(idPostagem);
    }

    public boolean salvar(DataSnapshot seguidoresSnapshot) {

        Map objeto = new HashMap();
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        String combinacaoId = "/" + getIdUsuario() + "/" + getId();
        objeto.put("/postagens" + combinacaoId, this);

        for (DataSnapshot seguidores : seguidoresSnapshot.getChildren()) {
            HashMap<String, Object> dadosSeguidor = new HashMap<>();
            dadosSeguidor.put("fotoPostagem", getCaminhoFoto());
            dadosSeguidor.put("descricao", getDescricao());
            dadosSeguidor.put("id", getId());
            dadosSeguidor.put("nomeUsuario", usuarioLogado.getNome());
            dadosSeguidor.put("fotoUsuario", usuarioLogado.getCaminhoFoto());

            String idAtualizacao = "/" + seguidores.getKey() + "/" + getId();
            objeto.put("/feed" + idAtualizacao, dadosSeguidor);
        }

        firebaseRef.updateChildren(objeto);
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
