package com.quanlyphim;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.quanlyphim.action.OnClickCategoryListener;
import com.quanlyphim.databinding.ItemCategoryBinding;
import com.quanlyphim.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private final ArrayList<Category> list = new ArrayList<>();
    private OnClickCategoryListener onClickCategoryListener;
    private int selectedPosition = 0;
    public CategoryAdapter(OnClickCategoryListener onClickCategoryListener) {
        this.onClickCategoryListener = onClickCategoryListener;
    }

    public void submit(List<Category> list) {
        this.list.clear();
        if (!list.isEmpty()) {
            list.remove(0);
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(layoutInflater, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category item = list.get(position);
        holder.binding.tvName.setText(item.getName());

        if (selectedPosition == position) {
            holder.binding.itemCategory.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_cate_selected));
        } else {
            holder.binding.itemCategory.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_item_film));
        }
        holder.binding.itemCategory.setOnClickListener(
                view -> {
                    int previousPosition = selectedPosition;
                    selectedPosition = position;

                    notifyItemChanged(previousPosition);
                    notifyItemChanged(selectedPosition);
                    onClickCategoryListener.onClickCategory(item);
                }
        );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder {
    final ItemCategoryBinding binding;

    public CategoryViewHolder(ItemCategoryBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
