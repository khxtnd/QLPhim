package com.quanlyphim.action;


import com.quanlyphim.model.Film;

public interface OnClickFilmListener {
    void onClickAsset(Film film);
    void deleteAsset(Film film);
}
