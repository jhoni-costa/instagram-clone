package br.com.jhonicosta.instagram_clone.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.helper.ConfiguracaoFirebase;
import br.com.jhonicosta.instagram_clone.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {

    private Usuario usuarioSelect;
    private Button buttonAcaoPerfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;

    private CircleImageView imagemPerfil;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioAmigoRef;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        usuariosRef = ConfiguracaoFirebase.getFirebase().child("usuarios");
        inicializaComponentes();

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioSelect = (Usuario) bundle.getSerializable("usuarioSelecionado");
            getSupportActionBar().setTitle(usuarioSelect.getNome());
        }

        if (usuarioSelect.getCaminhoFoto() != null) {
            Uri uri = Uri.parse(usuarioSelect.getCaminhoFoto());
            Glide.with(PerfilAmigoActivity.this).load(uri).into(imagemPerfil);
        }
    }

    private void inicializaComponentes() {
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        buttonAcaoPerfil.setText("Seguir");
        imagemPerfil = findViewById(R.id.imagePerfil);
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textSeguidores);
        textSeguindo = findViewById(R.id.textSeguindo);
        imagemPerfil = findViewById(R.id.imagePerfil);
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textSeguidores);
        textSeguindo = findViewById(R.id.textSeguindo);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosPerfilAmigo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioAmigoRef.removeEventListener(valueEventListener);

    }

    private void recuperarDadosPerfilAmigo() {
        usuarioAmigoRef = usuariosRef.child(usuarioSelect.getId());
        valueEventListener = usuarioAmigoRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        String postagens = String.valueOf(usuario.getPostagens());
                        String seguindo = String.valueOf(usuario.getSeguindo());
                        String seguidores = String.valueOf(usuario.getSeguidores());

                        textPublicacoes.setText(postagens);
                        textSeguidores.setText(seguidores);
                        textSeguindo.setText(seguindo);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
