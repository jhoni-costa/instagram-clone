package br.com.jhonicosta.instagram_clone.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.firebase.UsuarioFirebase;
import br.com.jhonicosta.instagram_clone.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private TextView nomeBox, emailBox, senhaBox;
    private Button btnCadastrar;
    private ProgressBar progressBar;

    private UsuarioFirebase firebase;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        firebase = new UsuarioFirebase(this);

        nomeBox = findViewById(R.id.cadastro_edit_nome);
        emailBox = findViewById(R.id.cadastro_edit_email);
        senhaBox = findViewById(R.id.cadastro_edit_password);
        btnCadastrar = findViewById(R.id.cadastro_botao_cadastrar);
        progressBar = findViewById(R.id.login_progress_bar);

        progressBar.setVisibility(View.GONE);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                usuario = new Usuario();

                usuario.setNome(nomeBox.getText().toString());
                usuario.setEmail(emailBox.getText().toString());
                usuario.setSenha(senhaBox.getText().toString());

                if (!usuario.getNome().isEmpty()) {
                    if (!usuario.getEmail().isEmpty()) {
                        if (!usuario.getSenha().isEmpty()) {
                            firebase.cadastrar(usuario);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Informe uma senha!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Informe um e-mail valido!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Informe um nome! " + usuario.getNome(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
