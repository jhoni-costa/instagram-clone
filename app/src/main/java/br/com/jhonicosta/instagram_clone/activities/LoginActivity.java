package br.com.jhonicosta.instagram_clone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.firebase.UsuarioFirebase;
import br.com.jhonicosta.instagram_clone.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextView cadastrar;
    private EditText emailBox, senhaBox;
    private ProgressBar progressBar;

    private Usuario usuario;
    private UsuarioFirebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usuario = new Usuario();
        firebase = new UsuarioFirebase(this);

        firebase.isAuth();

        emailBox = findViewById(R.id.login_edit_email);
        emailBox.requestFocus();
        senhaBox = findViewById(R.id.login_edit_password);
        progressBar = findViewById(R.id.login_progress_bar);

        cadastrar = findViewById(R.id.login_txt_cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            }
        });
    }

    public void login(View view) {
        usuario.setEmail(emailBox.getText().toString());
        usuario.setSenha(senhaBox.getText().toString());

        progressBar.setVisibility(View.VISIBLE);

        if (!usuario.getEmail().isEmpty()) {
            if (!usuario.getSenha().isEmpty()) {
                firebase.logar(usuario);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "Preencha a senha corretamente!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Preencha o e-mail corretamente!", Toast.LENGTH_SHORT).show();
        }
    }
}
