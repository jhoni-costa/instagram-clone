package br.com.jhonicosta.instagram_clone.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.jhonicosta.instagram_clone.R;

public class PesquisaFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;

    public PesquisaFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        searchView = view.findViewById(R.id.searchViewPesquisa);
        recyclerView = view.findViewById(R.id.reciclerViewPesquisa);

        searchView.setQueryHint("Buscar usuários");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("onQueryTextChange", "texto digitando " + newText);
                return true;
            }
        });

        return view;
    }

}
