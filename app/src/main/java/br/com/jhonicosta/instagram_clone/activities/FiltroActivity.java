package br.com.jhonicosta.instagram_clone.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.adapter.AdapterMiniaturas;
import br.com.jhonicosta.instagram_clone.helper.ConfiguracaoFirebase;
import br.com.jhonicosta.instagram_clone.helper.RecyclerItemClickListener;
import br.com.jhonicosta.instagram_clone.helper.UsuarioFirebase;
import br.com.jhonicosta.instagram_clone.model.Postagem;
import br.com.jhonicosta.instagram_clone.model.Usuario;

public class FiltroActivity extends AppCompatActivity {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private ImageView foto;
    private RecyclerView recyclerFiltros;
    private TextInputEditText textDescricao;
    private AlertDialog dialog;

    private Bitmap imagem, imagemFiltro;

    private AdapterMiniaturas adapterMiniaturas;

    private Usuario usuarioLogado;
    private List<ThumbnailItem> listaFiltros;
    private String idUsuarioLogado;

    private DatabaseReference firebaseRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioLogadoRef;
    private DataSnapshot seguidoresSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        listaFiltros = new ArrayList<>();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        usuariosRef = ConfiguracaoFirebase.getFirebase().child("usuarios");

        inicializaComponentes();

        recuperarDadosPostagem();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Filtros");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] dados = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dados, 0, dados.length);
            imagemFiltro = imagem.copy(imagem.getConfig(), true);
            foto.setImageBitmap(imagem);

            adapterMiniaturas = new AdapterMiniaturas(listaFiltros, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerFiltros.setLayoutManager(layoutManager);
            recyclerFiltros.setAdapter(adapterMiniaturas);

            recyclerFiltros.addOnItemTouchListener(new RecyclerItemClickListener(
                    getApplicationContext(),
                    recyclerFiltros,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            ThumbnailItem item = listaFiltros.get(position);
                            imagemFiltro = imagem.copy(imagem.getConfig(), true);
                            Filter filter = item.filter;
                            foto.setImageBitmap(filter.processFilter(imagemFiltro));
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                        }

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        }
                    }
            ));

            recuperarFitros();
        }
    }

    private void inicializaComponentes() {
        foto = findViewById(R.id.imagemFoto);
        recyclerFiltros = findViewById(R.id.recyclerFiltros);
        textDescricao = findViewById(R.id.textDescricaoFiltro);
    }

    private void abrirDialogCarregamento(String titulo) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(titulo);
        alert.setCancelable(false);
        alert.setView(R.layout.carregamento);

        dialog = alert.create();
        dialog.show();

    }

    private void recuperarDadosPostagem() {

        abrirDialogCarregamento("Carregando dados, aguarde!!");
        usuarioLogadoRef = usuariosRef.child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usuarioLogado = dataSnapshot.getValue(Usuario.class);

                        final DatabaseReference seguidoresRef = firebaseRef
                                .child("seguidores")
                                .child(idUsuarioLogado);
                        seguidoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                seguidoresSnapshot = dataSnapshot;
                                dialog.cancel();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void recuperarFitros() {

        ThumbnailsManager.clearThumbs();
        listaFiltros.clear();
        ThumbnailItem item = new ThumbnailItem();
        item.image = imagem;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb(item);

        List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());
        for (Filter f : filters) {
            ThumbnailItem itemFiltro = new ThumbnailItem();
            itemFiltro.image = imagem;
            itemFiltro.filter = f;
            itemFiltro.filterName = f.getName();

            ThumbnailsManager.addThumb(itemFiltro);
        }
        listaFiltros.addAll(ThumbnailsManager.processThumbs(getApplicationContext()));
        adapterMiniaturas.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_salvar_postagem:
                publicarPostagem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void publicarPostagem() {

        abrirDialogCarregamento("Salvando postagem, aguarde!!");
        final Postagem postagem = new Postagem();
        postagem.setIdUsuario(idUsuarioLogado);
        postagem.setDescricao(textDescricao.getText().toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagemFiltro.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] dadosImagem = baos.toByteArray();

        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        StorageReference imagemRef = storageReference
                .child("imagens")
                .child("postagens")
                .child(postagem.getId() + ".jpeg");

        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FiltroActivity.this,
                        "Erro ao salvar  imagem",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri url = taskSnapshot.getDownloadUrl();
                postagem.setCaminhoFoto(url.toString());

                int qtdPostagens = usuarioLogado.getPostagens() + 1;
                usuarioLogado.setPostagens(qtdPostagens);
                usuarioLogado.atualizarQtdPostagem();

                if (postagem.salvar(seguidoresSnapshot)) {

                    Toast.makeText(FiltroActivity.this,
                            "Sucesso ao salvar postagem!",
                            Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    finish();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_filtro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
