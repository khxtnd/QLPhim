package com.quanlyphim;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.quanlyphim.action.OnClickCategoryListener;
import com.quanlyphim.databinding.ItemRoomBinding;
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
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRoomBinding binding = ItemRoomBinding.inflate(layoutInflater, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category item = list.get(position);
        holder.binding.tvName.setText(item.getName());
        holder.binding.tvDesc.setText(item.getDesc());
        holder.binding.itemRoom.setOnClickListener(view -> onClickCategoryListener.onClickRoom(item));

        holder.binding.ivDelete.setOnClickListener(view -> onClickCategoryListener.deleteRoom(item.getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder {
    final ItemRoomBinding binding;

    public CategoryViewHolder(ItemRoomBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
