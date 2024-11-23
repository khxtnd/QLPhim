package com.quanlyphim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quanlyphim.action.OnClickFilmListener;
import com.quanlyphim.databinding.ItemFilmBinding;
import com.quanlyphim.databinding.ItemStarBinding;
import com.quanlyphim.model.Film;

import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarViewHolder> {

    private Integer star;

    public StarAdapter(Integer star) {
        this.star = star;
    }


    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemStarBinding binding = ItemStarBinding.inflate(layoutInflater, parent, false);
        return new StarViewHolder(binding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        if (position + 1 > star) {
            holder.binding.ivStar.setImageResource(R.drawable.ic_star_gray_24);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

class StarViewHolder extends RecyclerView.ViewHolder {
    final ItemStarBinding binding;

    public StarViewHolder(ItemStarBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
