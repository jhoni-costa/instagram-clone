package br.com.jhonicosta.instagram_clone.firebase;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.jhonicosta.instagram_clone.activities.MainActivity;
import br.com.jhonicosta.instagram_clone.config.FirebaseConfig;
import br.com.jhonicosta.instagram_clone.model.Usuario;

public class UsuarioFirebase {

    private FirebaseAuth auth;
    private Activity activity;

    public UsuarioFirebase(Activity activity) {
        this.activity = activity;
        this.auth = FirebaseConfig.getFirebaseAuth();
    }

    public void cadastrar(final Usuario usuario) {

        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, usuario.getNome() + " cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                            activity.finish();
                        } else {

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

    public void logar(Usuario usuario) {
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                        } else {
                            String exception = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                exception = "E-mail e senha não conferem não conferem!";
                            } catch (FirebaseAuthInvalidUserException e) {
                                exception = "Usuário não cadastrado";
                            } catch (Exception e) {
                                exception = "Não foi possivel conectar com o servidor, tente novamente mais tarde..." + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(activity, exception, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void isAuth() {
        if (auth.getCurrentUser() != null) {
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        }
    }

    public void signOut() {
        try {
            auth.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
