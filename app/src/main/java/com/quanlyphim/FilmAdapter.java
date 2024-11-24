package com.quanlyphim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.quanlyphim.action.OnClickFilmListener;
import com.quanlyphim.databinding.ItemFilmBinding;
import com.quanlyphim.model.Film;

import java.util.ArrayList;
import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmViewHolder> {

    private final ArrayList<Film> list = new ArrayList<>();
    private OnClickFilmListener onClickFilmListener;
    private Context context;

    public FilmAdapter(Context context,OnClickFilmListener onClickFilmListener){
        this.onClickFilmListener = onClickFilmListener;
        this.context=context;
    }
    public void submit(List<Film> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFilmBinding binding = ItemFilmBinding.inflate(layoutInflater, parent, false);
        return new FilmViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film item = list.get(position);
        holder.binding.tvName.setText(item.getName());
        holder.binding.tvCategory.setText(item.getCategory());

        holder.binding.itemFilm.setOnClickListener(view -> {
            onClickFilmListener.onClickFilm(item);
            holder.binding.itemFilm.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_cate_selected));
        });
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.img_star_war)
                .error(R.drawable.img_star_war)
                .into(holder.binding.ivFilm);
        holder.binding.recStar.setAdapter(new StarAdapter(item.getEvaluate()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class FilmViewHolder extends RecyclerView.ViewHolder {
    final ItemFilmBinding binding;

    public FilmViewHolder(ItemFilmBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
