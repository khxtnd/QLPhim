package com.quanlyphim;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.quanlyphim.action.OnClickFilmListener;
import com.quanlyphim.databinding.ItemAssetBinding;
import com.quanlyphim.model.Film;

import java.util.ArrayList;
import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmViewHolder> {

    private final ArrayList<Film> list = new ArrayList<>();
    private OnClickFilmListener onClickFilmListener;

    public FilmAdapter(OnClickFilmListener onClickFilmListener){
        this.onClickFilmListener = onClickFilmListener;
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
        ItemAssetBinding binding = ItemAssetBinding.inflate(layoutInflater, parent, false);
        return new FilmViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film item = list.get(position);
        holder.binding.tvName.setText(item.getName());
        holder.binding.tvCate.setText(item.getCategory());
        holder.binding.tvPosition.setText(item.getImage());
        holder.binding.tvPrice.setText(item.getRate()+" K");

        holder.binding.itemAsset.setOnClickListener(view -> onClickFilmListener.onClickAsset(item));

        holder.binding.ivDelete.setOnClickListener(view -> onClickFilmListener.deleteAsset(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class FilmViewHolder extends RecyclerView.ViewHolder {
    final ItemAssetBinding binding;

    public FilmViewHolder(ItemAssetBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
