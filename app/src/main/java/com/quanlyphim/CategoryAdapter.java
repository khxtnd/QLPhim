package com.quanlyphim;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quanlyphim.action.OnClickCategoryListener;
import com.quanlyphim.databinding.ItemCategoryBinding;
import com.quanlyphim.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private final ArrayList<Category> list = new ArrayList<>();
    private OnClickCategoryListener onClickCategoryListener;

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
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category item = list.get(position);
        holder.binding.tvName.setText(item.getName());
        holder.binding.itemCategory.setOnClickListener(
                view -> {
                    if (position > 0) {
                        onClickCategoryListener.onClickRoom(item);
                    }
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
