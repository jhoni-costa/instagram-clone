package br.com.jhonicosta.instagram_clone.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.helper.UsuarioFirebase;
import br.com.jhonicosta.instagram_clone.model.Comentario;
import br.com.jhonicosta.instagram_clone.model.Usuario;

public class ComentariosActivity extends AppCompatActivity {

    private EditText editComentario;
    private String idPostagem;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        editComentario = findViewById(R.id.editComentario);
        usuario = UsuarioFirebase.getDadosUsuarioLogado();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Comentários");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idPostagem = bundle.getString("idPostagem");
        }
    }

    public void salvarComentario(View view) {
        String textoComentario = editComentario.getText().toString();
        if (textoComentario != null && !textoComentario.equals("")) {
            Comentario comentario = new Comentario();
            comentario.setIdPostagem(idPostagem);
            comentario.setIdUsuario(usuario.getId());
            comentario.setNomeUsuario(usuario.getNome());
            comentario.setCaminhoFoto(usuario.getCaminhoFoto());
            comentario.setIdComentario(textoComentario);
            if (comentario.salvar()) {
                Toast.makeText(this, "Comentário salvo com sucesso!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Insira o comentário antes de salvar!", Toast.LENGTH_SHORT).show();
        }
        editComentario.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;

    }
}
