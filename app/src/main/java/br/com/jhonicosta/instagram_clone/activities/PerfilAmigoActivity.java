package br.com.jhonicosta.instagram_clone.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.model.Usuario;

public class PerfilAmigoActivity extends AppCompatActivity {

    private Usuario usuarioSelect;
    private Button buttonAcaoPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        buttonAcaoPerfil.setText("Seguir");

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
