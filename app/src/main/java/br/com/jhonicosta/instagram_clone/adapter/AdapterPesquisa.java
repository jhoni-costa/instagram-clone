package br.com.jhonicosta.instagram_clone.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPesquisa extends RecyclerView.Adapter<AdapterPesquisa.MyViewHolder> {

    private List<Usuario> listaUsuario;
    private Context context;

    public AdapterPesquisa(List<Usuario> listaUsuario, Context context) {
        this.listaUsuario = listaUsuario;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adaptar_pesquisa_usuario, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Usuario usuario = listaUsuario.get(i);
        myViewHolder.nome.setText(usuario.getNome());
        myViewHolder.foto.setImageResource(R.drawable.avatar);

        if (usuario.getCaminhoFoto() != null) {
            myViewHolder.foto.setImageResource(R.drawable.avatar);
            Uri uri = Uri.parse(usuario.getCaminhoFoto());
            Glide.with(context).load(uri).into(myViewHolder.foto);
        }
    }

    @Override
    public int getItemCount() {
        return listaUsuario.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView foto;
        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imagemFotoPesquisa);
            nome = itemView.findViewById(R.id.textNomePesquisa);
        }
    }
}
