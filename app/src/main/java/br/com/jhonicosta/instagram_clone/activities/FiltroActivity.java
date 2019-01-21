package br.com.jhonicosta.instagram_clone.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import br.com.jhonicosta.instagram_clone.R;

public class FiltroActivity extends AppCompatActivity {

    private ImageView foto;
    private Bitmap imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        foto = findViewById(R.id.imagemFoto);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] dados = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dados, 0, dados.length);
            foto.setImageBitmap(imagem);
        }
    }
}
