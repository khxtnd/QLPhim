package com.quanlyphim;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.quanlyphim.action.OnClickFilmListener;
import com.quanlyphim.action.OnClickCategoryListener;
import com.quanlyphim.databinding.ActivityMainBinding;
import com.quanlyphim.model.Film;
import com.quanlyphim.model.Category;

public class MainActivity extends AppCompatActivity implements OnClickCategoryListener, OnClickFilmListener {
    private ActivityMainBinding binding;
    private CategoryAdapter categoryAdapter;
    private FilmAdapter filmAdapter;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHandler = new DatabaseHandler(this);
        binding.btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity(Constants.addCate, null, null);
            }
        });

        binding.btnCreateAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity(Constants.addFilm, null, null);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllCategory();
        showAllFilm();
    }

    @Override
    public void onClickRoom(Category category) {
        moveDetailActivity(Constants.updateCate, category, null);
    }

    @Override
    public void onClickAsset(Film film) {
        moveDetailActivity(Constants.updateAsset, null, film);
    }

    @Override
    public void deleteAsset(Film film) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getResources().getString(R.string.delete_film));
        builder.setMessage(this.getResources().getString(R.string.confirm_delete));

        builder.setPositiveButton(this.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHandler.deleteFilm(film.getId());
                filmAdapter.submit(dbHandler.getAllFilms());
            }
        });

        builder.setNegativeButton(this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void deleteRoom(Integer id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getResources().getString(R.string.delete_category));
        builder.setMessage(this.getResources().getString(R.string.confirm_delete));

        builder.setPositiveButton(this.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHandler.deleteCategory(id);
                categoryAdapter.submit(dbHandler.getAllCategory());
            }
        });

        builder.setNegativeButton(this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAllCategory() {
        categoryAdapter = new CategoryAdapter(this);
        binding.recCategory.setAdapter(categoryAdapter);
        categoryAdapter.submit(dbHandler.getAllCategory());
    }

    private void showAllFilm() {
        filmAdapter = new FilmAdapter(this);
        binding.recFilm.setAdapter(filmAdapter);
        filmAdapter.submit(dbHandler.getAllFilms());
    }

    private void moveDetailActivity(String valueIntent, Category category, Film film) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.nameIntent, valueIntent);
        if (valueIntent.equals(Constants.updateCate)) {
            intent.putExtra(Constants.dataIntent, category);
        } else if (valueIntent.equals(Constants.updateAsset)) {
            intent.putExtra(Constants.dataIntent, film);
        }
        startActivity(intent);
    }

}