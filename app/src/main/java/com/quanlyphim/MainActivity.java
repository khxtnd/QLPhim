package com.quanlyphim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.quanlyphim.action.OnClickCategoryListener;
import com.quanlyphim.action.OnClickFilmListener;
import com.quanlyphim.databinding.ActivityMainBinding;
import com.quanlyphim.model.Category;
import com.quanlyphim.model.Film;

public class MainActivity extends AppCompatActivity implements OnClickCategoryListener, OnClickFilmListener {
    private ActivityMainBinding binding;
    private CategoryAdapter categoryAdapter;
    private FilmAdapter filmAdapter;
    private DatabaseHandler dbHandler;
    private Category cateSelected = new Category();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHandler = new DatabaseHandler(this);
        binding.btnAddCate.setOnClickListener(v -> moveDetailActivity(Constants.addCate, null, null));

        binding.btnAddFilm.setOnClickListener(v -> moveDetailActivity(Constants.addFilm, null, null));

        binding.btnShowCate.setOnClickListener(v -> {
            filmAdapter.submit(dbHandler.getAllFilmStarHot(4));
        });

        binding.btnUpdateCate.setOnClickListener(v -> {
            moveDetailActivity(Constants.updateCate, cateSelected, null);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllCategory();
        showAllFilm();
    }

    @Override
    public void onClickCategory(Category category) {
        if (category.getId() == 1) {
            filmAdapter.submit(dbHandler.getAllFilms());
            binding.btnUpdateCate.setVisibility(View.GONE);
        } else {
            filmAdapter.submit(dbHandler.getFilmByCategory(category.getId()));
            binding.btnUpdateCate.setVisibility(View.VISIBLE);
        }

        cateSelected = category;
    }

    @Override
    public void onClickFilm(Film film) {
        moveDetailActivity(Constants.updateFilm, null, film);
    }

    private void showAllCategory() {
        categoryAdapter = new CategoryAdapter(this);
        binding.recCategory.setAdapter(categoryAdapter);
        categoryAdapter.submit(dbHandler.getAllCategory());
    }

    private void showAllFilm() {
        filmAdapter = new FilmAdapter(MainActivity.this, this);
        binding.recFilm.setAdapter(filmAdapter);
        filmAdapter.submit(dbHandler.getAllFilms());
    }

    private void moveDetailActivity(String valueIntent, Category category, Film film) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.nameIntent, valueIntent);
        if (valueIntent.equals(Constants.updateCate)) {
            intent.putExtra(Constants.dataIntent, category);
        } else if (valueIntent.equals(Constants.updateFilm)) {
            intent.putExtra(Constants.dataIntent, film);
        }
        startActivity(intent);
    }

}