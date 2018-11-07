package br.com.jhonicosta.instagram_clone.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.jhonicosta.instagram_clone.activities.CadastroActivity;
import br.com.jhonicosta.instagram_clone.activities.MainActivity;
import br.com.jhonicosta.instagram_clone.config.FirebaseConfig;
import br.com.jhonicosta.instagram_clone.model.Usuario;

public class UsuarioFirebase {

    private FirebaseAuth auth;
    private CadastroActivity activity;

    public UsuarioFirebase(CadastroActivity activity) {
        this.activity = activity;
        this.auth = FirebaseConfig.getFirebaseAuth();
    }

    public void cadastrar(final Usuario usuario) {
        activity.getProgressBar().setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, usuario.getNome() + " cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                            activity.finish();
                        } else {
                            activity.getProgressBar().setVisibility(View.GONE);

                            String exception = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                exception = "Digite uma senha mais forte!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                exception = "Digite um e-mail válido!";
                            } catch (FirebaseAuthUserCollisionException e) {
                                exception = "Cliente já cadastrado";
                            } catch (Exception e) {
                                exception = "Erro bizarro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(activity, exception, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
