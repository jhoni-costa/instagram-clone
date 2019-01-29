package br.com.jhonicosta.instagram_clone.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.model.Postagem;
import br.com.jhonicosta.instagram_clone.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class VisualizarPostagemActivity extends AppCompatActivity {

    private CircleImageView imagemPerfil;
    private ImageView imagemPostagem;
    private TextView nomeUsuario, qtdCurtidas, textDescricao, textVisualizar;

    private Usuario usuarioSelecionado;
    private Postagem postagemSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_postagem);

        inicializarComponentes();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Visualizar postagem");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuario");
            postagemSelecionada = (Postagem) bundle.getSerializable("postagem");

            if (usuarioSelecionado.getCaminhoFoto() != null) {
                Glide.with(VisualizarPostagemActivity.this)
                        .load(usuarioSelecionado.getCaminhoFoto())
                        .into(imagemPerfil);
                nomeUsuario.setText(usuarioSelecionado.getNome());
            }

            if (postagemSelecionada.getCaminhoFoto() != null) {
                Glide.with(VisualizarPostagemActivity.this)
                        .load(postagemSelecionada.getCaminhoFoto())
                        .into(imagemPostagem);
            }

            if (postagemSelecionada.getDescricao() != null) {
                textDescricao.setText(postagemSelecionada.getDescricao());
            }
        }
    }

    private void inicializarComponentes() {
        imagemPerfil = findViewById(R.id.imagePerfilPublicacao);
        imagemPostagem = findViewById(R.id.imagePublicacao);
        nomeUsuario = findViewById(R.id.textNomeUsuario);
        qtdCurtidas = findViewById(R.id.textCurtidas);
        textDescricao = findViewById(R.id.textDescricao);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
