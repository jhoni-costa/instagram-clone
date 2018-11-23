package br.com.jhonicosta.instagram_clone.fragmensts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.activities.EditarPerfilActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    private ProgressBar progressBar;
    private CircleImageView imagePerfil;
    public GridView gridView;
    private TextView qtdPublicacoes, qtdSeguidores, qtdSeguindo;
    private Button btnEditarPerfil;

    public PerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        progressBar = view.findViewById(R.id.progressBarPerfil);
        imagePerfil = view.findViewById(R.id.imagePerfil);
        gridView = view.findViewById(R.id.gripPerfil);
        qtdPublicacoes = view.findViewById(R.id.qtdPublicacoes);
        qtdSeguidores = view.findViewById(R.id.qtdSeguidores);
        qtdSeguindo = view.findViewById(R.id.qtdSeguindo);
        btnEditarPerfil = view.findViewById(R.id.buttonEditarPerfil);

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });

        return view;
    }

}
