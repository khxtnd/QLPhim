package com.quanlyphim.action;


import com.quanlyphim.model.Category;

public interface OnClickCategoryListener {
    void onClickRoom(Category category);
    void deleteRoom(Integer id);
}
