package br.com.jhonicosta.instagram_clone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.helper.ConfiguracaoFirebase;
import br.com.jhonicosta.instagram_clone.model.Usuario;

public class PesquisaFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;

    private List<Usuario> listaUsuarios = new ArrayList<>();
    private DatabaseReference usuariosRef;

    public PesquisaFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        searchView = view.findViewById(R.id.searchViewPesquisa);
        recyclerView = view.findViewById(R.id.reciclerViewPesquisa);

        usuariosRef = ConfiguracaoFirebase.getFirebase().child("usuarios");

        searchView.setQueryHint("Buscar usuários");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String textoDigitado = newText.toUpperCase();
                pesquisarUsuarios(textoDigitado);
                return true;
            }
        });

        return view;
    }

    private void pesquisarUsuarios(String texto) {
        listaUsuarios.clear();

        if (texto.length() > 0) {
            Query query = usuariosRef.orderByChild("nome")
                    .startAt(texto)
                    .endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        listaUsuarios.add(ds.getValue(Usuario.class));
                    }

                    int total = listaUsuarios.size();
                    Log.i("totalUsuarios", "total " + total);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
