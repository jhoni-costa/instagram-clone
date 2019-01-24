package br.com.jhonicosta.instagram_clone.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.adapter.AdapterGrid;
import br.com.jhonicosta.instagram_clone.helper.ConfiguracaoFirebase;
import br.com.jhonicosta.instagram_clone.helper.UsuarioFirebase;
import br.com.jhonicosta.instagram_clone.model.Postagem;
import br.com.jhonicosta.instagram_clone.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {

    private Button buttonAcaoPerfil;
    private CircleImageView imagePerfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private GridView gridViewPerfil;

    private AdapterGrid adapterGrid;

    private DatabaseReference firebaseRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioAmigoRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference seguidoresRef;
    private DatabaseReference postagensUsuarioRef;
    private ValueEventListener valueEventListenerPerfilAmigo;

    private Usuario usuarioSelecionado;
    private Usuario usuarioLogado;
    private List<Postagem> postagens;

    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        firebaseRef = ConfiguracaoFirebase.getFirebase();
        usuariosRef = firebaseRef.child("usuarios");
        seguidoresRef = firebaseRef.child("seguidores");
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        inicializarComponentes();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Pefil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            postagensUsuarioRef = ConfiguracaoFirebase.getFirebase().child("postagens")
                    .child(usuarioSelecionado.getId());

            getSupportActionBar().setTitle(usuarioSelecionado.getNome());

            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();
            if (caminhoFoto != null) {
                Uri url = Uri.parse(caminhoFoto);
                Glide.with(PerfilAmigoActivity.this)
                        .load(url)
                        .into(imagePerfil);
            }
        }

        inicializarImageLoader();

        carregarFotosPostagens();


        gridViewPerfil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Postagem postagem = postagens.get(position);
                Intent i = new Intent(getApplicationContext(), VisualizarPostagemActivity.class);
                i.putExtra("postagem", postagem);
                i.putExtra("usuario", usuarioSelecionado);
                startActivity(i);
            }
        });
    }

    public void carregarFotosPostagens() {
        postagens = new ArrayList<>();
        postagensUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int tamanhoImagem = getResources().getDisplayMetrics().widthPixels / 3;
                gridViewPerfil.setColumnWidth(tamanhoImagem);

                List<String> urlsFotos = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Postagem postagem = ds.getValue(Postagem.class);
                    postagens.add(postagem);
                    urlsFotos.add(postagem.getCaminhoFoto());
                }

                adapterGrid = new AdapterGrid(getApplicationContext(), R.layout.grid_postagem, urlsFotos);
                gridViewPerfil.setAdapter(adapterGrid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void inicializarImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    private void recuperarDadosUsuarioLogado() {

        usuarioLogadoRef = usuariosRef.child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usuarioLogado = dataSnapshot.getValue(Usuario.class);
                        verificaSegueUsuarioAmigo();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }
        );
    }

    private void verificaSegueUsuarioAmigo() {

        DatabaseReference seguidorRef = seguidoresRef
                .child(usuarioSelecionado.getId())
                .child(idUsuarioLogado);

        seguidorRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.i("dadosUsuario", ": Seguindo");
                            habilitarBotaoSeguir(true);
                        } else {
                            Log.i("dadosUsuario", ": seguir");
                            habilitarBotaoSeguir(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }
        );
    }

    private void habilitarBotaoSeguir(boolean segueUsuario) {

        if (segueUsuario) {
            buttonAcaoPerfil.setText("Seguindo");
        } else {

            buttonAcaoPerfil.setText("Seguir");

            buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    salvarSeguidor(usuarioLogado, usuarioSelecionado);
                }
            });
        }
    }

    private void salvarSeguidor(Usuario uLogado, Usuario uAmigo) {

        HashMap<String, Object> dadosUsuarioLogado = new HashMap<>();
        dadosUsuarioLogado.put("nome", uLogado.getNome());
        dadosUsuarioLogado.put("caminhoFoto", uLogado.getCaminhoFoto());
        DatabaseReference seguidorRef = seguidoresRef
                .child(uAmigo.getId())
                .child(uLogado.getId());
        seguidorRef.setValue(dadosUsuarioLogado);

        buttonAcaoPerfil.setText("Seguindo");
        buttonAcaoPerfil.setOnClickListener(null);

        int seguindo = uLogado.getSeguindo() + 1;
        HashMap<String, Object> dadosSeguindo = new HashMap<>();
        dadosSeguindo.put("seguindo", seguindo);

        DatabaseReference usuarioSeguindo = usuariosRef
                .child(uLogado.getId());
        usuarioSeguindo.updateChildren(dadosSeguindo);

        int seguidores = uAmigo.getSeguidores() + 1;
        HashMap<String, Object> dadosSeguidores = new HashMap<>();
        dadosSeguidores.put("seguidores", seguidores);

        DatabaseReference usuarioSeguidores = usuariosRef
                .child(uAmigo.getId());
        usuarioSeguidores.updateChildren(dadosSeguidores);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosPerfilAmigo();
        recuperarDadosUsuarioLogado();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioAmigoRef.removeEventListener(valueEventListenerPerfilAmigo);
    }

    private void recuperarDadosPerfilAmigo() {

        usuarioAmigoRef = usuariosRef.child(usuarioSelecionado.getId());
        valueEventListenerPerfilAmigo = usuarioAmigoRef.addValueEventListener(
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

    private void inicializarComponentes() {
        imagePerfil = findViewById(R.id.imagePerfilPublicacao);
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textSeguidores);
        textSeguindo = findViewById(R.id.textSeguindo);
        buttonAcaoPerfil.setText("Carregando");
        gridViewPerfil = findViewById(R.id.gridViewPerfil);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
