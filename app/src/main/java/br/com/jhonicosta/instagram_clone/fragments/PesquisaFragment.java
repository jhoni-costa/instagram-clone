package br.com.jhonicosta.instagram_clone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.activities.PerfilAmigoActivity;
import br.com.jhonicosta.instagram_clone.adapter.AdapterPesquisa;
import br.com.jhonicosta.instagram_clone.helper.ConfiguracaoFirebase;
import br.com.jhonicosta.instagram_clone.helper.RecyclerItemClickListener;
import br.com.jhonicosta.instagram_clone.model.Usuario;

public class PesquisaFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;

    private List<Usuario> listaUsuarios = new ArrayList<>();
    private DatabaseReference usuariosRef;

    private AdapterPesquisa adapterPesquisa;

    public PesquisaFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        searchView = view.findViewById(R.id.searchViewPesquisa);
        recyclerView = view.findViewById(R.id.reciclerViewPesquisa);

        usuariosRef = ConfiguracaoFirebase.getFirebase().child("usuarios");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterPesquisa = new AdapterPesquisa(listaUsuarios, getActivity());
        recyclerView.setAdapter(adapterPesquisa);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Usuario usuarioSelecionado = listaUsuarios.get(position);

                        Intent i = new Intent(getActivity(), PerfilAmigoActivity.class);
                        i.putExtra("usuarioSelecionado", usuarioSelecionado);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

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

        if (texto.length() >= 2) {
            Query query = usuariosRef.orderByChild("nome")
                    .startAt(texto)
                    .endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listaUsuarios.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        listaUsuarios.add(ds.getValue(Usuario.class));
                    }
                    adapterPesquisa.notifyDataSetChanged();
//                    int total = listaUsuarios.size();
//                    Log.i("totalUsuarios", "total " + total);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
