package br.com.jhonicosta.instagram_clone.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;

import br.com.jhonicosta.instagram_clone.R;

public class FiltroActivity extends AppCompatActivity {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private ImageView foto;
    private Bitmap imagem, imagemFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        foto = findViewById(R.id.imagemFoto);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Filtros");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] dados = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dados, 0, dados.length);
            foto.setImageBitmap(imagem);

            imagemFiltro = imagem.copy(imagem.getConfig(), true);
            Filter filter = FilterPack.getAmazonFilter(getApplicationContext());
            foto.setImageBitmap(filter.processFilter(imagemFiltro));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_salvar_postagem:

                break;
        }
        return super.onOptionsItemSelected(item);
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
