package br.com.jhonicosta.instagram_clone.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import br.com.jhonicosta.instagram_clone.R;
import br.com.jhonicosta.instagram_clone.helper.ConfiguracaoFirebase;
import br.com.jhonicosta.instagram_clone.helper.UsuarioFirebase;
import br.com.jhonicosta.instagram_clone.model.Feed;
import br.com.jhonicosta.instagram_clone.model.PostagemCurtida;
import br.com.jhonicosta.instagram_clone.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private List<Feed> listaFeed;
    private Context context;

    public AdapterFeed(List<Feed> listaFeed, Context context) {
        this.listaFeed = listaFeed;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_feed, viewGroup, false);
        return new AdapterFeed.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final Feed feed = listaFeed.get(i);
        final Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        Uri uriFotoUsuario = Uri.parse(feed.getFotoUsuario());
        Uri uriFotoPostagem = Uri.parse(feed.getFotoPostagem());

        Glide.with(context).load(uriFotoUsuario).into(myViewHolder.fotoPerfil);
        Glide.with(context).load(uriFotoPostagem).into(myViewHolder.fotoPostagem);

        myViewHolder.descricao.setText(feed.getDescricao());
        myViewHolder.nome.setText(feed.getNomeUsuario());

        DatabaseReference curtidasRef = ConfiguracaoFirebase.getFirebase()
                .child("postagens-curtidas")
                .child(feed.getId());
        curtidasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int qtdCurtida = 0;
                if (dataSnapshot.hasChild("qtdCurtidas")) {
                    PostagemCurtida pc = dataSnapshot.getValue(PostagemCurtida.class);
                    qtdCurtida = pc.getQtdCurtidas();
                }

                if (dataSnapshot.hasChild(usuarioLogado.getId())) {
                    myViewHolder.likeButton.setLiked(true);
                } else {
                    myViewHolder.likeButton.setLiked(false);
                }

                final PostagemCurtida curtida = new PostagemCurtida();
                curtida.setFeed(feed);
                curtida.setUsuario(usuarioLogado);
                curtida.setQtdCurtidas(qtdCurtida);

                myViewHolder.likeButton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        curtida.salvar();
                        myViewHolder.qtdCurtidas.setText(curtida.getQtdCurtidas() + " curtidas");
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        curtida.remover();
                        myViewHolder.qtdCurtidas.setText(curtida.getQtdCurtidas() + " curtidas");
                    }
                });
                myViewHolder.qtdCurtidas.setText(curtida.getQtdCurtidas() + " curtidas");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listaFeed.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView fotoPerfil;
        TextView nome, descricao, qtdCurtidas;
        ImageView fotoPostagem, visualizarComentarios;
        LikeButton likeButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            fotoPerfil = itemView.findViewById(R.id.imagePerfilPublicacao);
            fotoPostagem = itemView.findViewById(R.id.imagePublicacao);
            nome = itemView.findViewById(R.id.textNomeUsuario);
            qtdCurtidas = itemView.findViewById(R.id.textCurtidas);
            descricao = itemView.findViewById(R.id.textDescricao);
            visualizarComentarios = itemView.findViewById(R.id.imageComentarioFeed);
            likeButton = itemView.findViewById(R.id.likeButtonFeed);
        }
    }
}
