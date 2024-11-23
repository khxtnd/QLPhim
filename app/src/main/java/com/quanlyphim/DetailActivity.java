package com.quanlyphim;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.quanlyphim.action.OnClickFilmListener;
import com.quanlyphim.databinding.ActivityDetailBinding;
import com.quanlyphim.model.Category;
import com.quanlyphim.model.Film;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnClickFilmListener {

    private ActivityDetailBinding binding;
    private DatabaseHandler dbHandler;
    private String checkIntent = "";
    private FilmAdapter filmAdapter;

    private List<Film> allFilmUnknownCate;

    private List<Category> allCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        ;
        setContentView(binding.getRoot());
        dbHandler = new DatabaseHandler(this);

        checkIntent = getIntent().getStringExtra(Constants.nameIntent);
        switch (checkIntent) {
            case Constants.addCate:
                showViewAddCate();
                break;
            case Constants.addFilm:
                showViewAddFilm();
                break;
            case Constants.updateCate:
                showViewUpdateCate();
                break;
            default:
                showViewUpdateFilm();
                break;
        }


        binding.btnCancel.setOnClickListener(v -> finish());
    }

    private void showViewAddCate() {
        binding.tvBanner.setText(this.getResources().getText(R.string.add_category));
        binding.btnAddOrUpdate.setOnClickListener(v -> addCate());
    }

    private void addCate() {
        String name = binding.etNameCate.getText().toString();
        String desc = binding.etDescCate.getText().toString();

        if (!name.isEmpty() && !desc.isEmpty()) {
            dbHandler.insertCategory(new Category(name, desc));
            Toast.makeText(this, this.getResources().getString(R.string.create_success), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }

    private void showViewAddFilm() {
        binding.tvBanner.setText(this.getResources().getText(R.string.add_film));
        binding.linearCate.setVisibility(View.GONE);
        binding.linearFilm.setVisibility(View.VISIBLE);

        allCategories = dbHandler.getAllCategory();
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spRoom.setAdapter(adapter);
        binding.spRoom.setSelection(0);

        binding.btnAddOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFilm();
                finish();
            }
        });
    }

    private void addFilm() {
        String name = binding.etNameFilm.getText().toString();
        String cate = binding.etDescFilm.getText().toString();

        int position = binding.spRoom.getSelectedItemPosition();
        int star=binding.rateBar.getNumStars();

        if (!name.isEmpty() && !cate.isEmpty()) {
            if (true) {
                dbHandler.insertFilm(new Film(name, cate,"", "", star, 0));
            } else {
                List<Category> allCategories = dbHandler.getAllCategory();
                Integer roomId = allCategories.get(position - 1).getId();
                dbHandler.insertFilm(new Film(name, cate,"","", star, roomId));
            }
            Toast.makeText(this, this.getResources().getString(R.string.create_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }

    private void showViewUpdateCate() {
        binding.tvBanner.setText(this.getResources().getText(R.string.update_category));
        binding.btnAddOrUpdate.setText(this.getResources().getText(R.string.update));

        Category category = (Category) getIntent().getSerializableExtra(Constants.dataIntent);
        binding.etNameCate.setText(category.getName());
        binding.etDescCate.setText(category.getDesc());

        binding.linearAddFilmInCate.setVisibility(View.VISIBLE);
        filmAdapter = new FilmAdapter(this);
        binding.rec.setAdapter(filmAdapter);
        filmAdapter.submit(dbHandler.getFilmByCategory(category.getId()));

        allFilmUnknownCate = dbHandler.getAllAssetsUnknown();
        allFilmUnknownCate.add(0, new Film(0,"", this.getResources().getString(R.string.unknonwn), "", "", 0, 0));

        ArrayAdapter<Film> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allFilmUnknownCate);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spFilm.setAdapter(adapter);

        binding.btnAddFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int assetPosition = binding.spFilm.getSelectedItemPosition();
                Film film = (Film) binding.spFilm.getItemAtPosition(assetPosition);
                if (film != null && film.getId() != 0) {
                    film.setCategoryId(category.getId());
                    dbHandler.updateFilm(film);
                    filmAdapter.submit(dbHandler.getFilmByCategory(category.getId()));
                    updateSpinnerAssetUnknown();
                }

            }
        });
        binding.btnAddOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCate(category.getId());
            }
        });
    }

    private void updateSpinnerAssetUnknown() {
        allFilmUnknownCate = dbHandler.getAllAssetsUnknown();
        ArrayAdapter<Film> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allFilmUnknownCate);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spFilm.setAdapter(adapter);
        Toast.makeText(this, this.getResources().getString(R.string.add_asset_success), Toast.LENGTH_SHORT).show();

    }

    private void updateCate(Integer id) {
        String name = binding.etNameCate.getText().toString();
        String desc = binding.etDescCate.getText().toString();

        if (!name.isEmpty() && !desc.isEmpty()) {
            dbHandler.updateCategory(new Category(id, name, desc));
            Toast.makeText(this, this.getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }

    private void showViewUpdateFilm() {
        binding.tvBanner.setText(this.getResources().getText(R.string.update_film));

        binding.linearCate.setVisibility(View.GONE);
        binding.linearFilm.setVisibility(View.VISIBLE);
        binding.btnAddOrUpdate.setText(this.getResources().getText(R.string.update));

        Film film = (Film) getIntent().getSerializableExtra(Constants.dataIntent);
        if (film != null) {
            binding.etNameFilm.setText(film.getName());
            binding.rateBar.setRating(film.getRate());
        }

        allCategories = dbHandler.getAllCategory();
        Integer roomPosition = 0;
        allCategories.add(0, new Category(0, this.getResources().getString(R.string.unknown), ""));
        for (int i = 0; i < allCategories.size(); i++) {
            if (film != null && film.getCategoryId().equals(allCategories.get(i).getId())) {
                roomPosition = i;
            }
        }

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spRoom.setAdapter(adapter);
        if (film.getCategoryId() == 0) {
            binding.spRoom.setSelection(0);
        } else {
            binding.spRoom.setSelection(roomPosition);
        }
        binding.btnAddOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilm(film.getId());
            }
        });
    }

    private void updateFilm(Integer id) {
        String name = binding.etNameFilm.getText().toString();
        String desc = binding.etDescFilm.getText().toString();
        int roomPosition = binding.spRoom.getSelectedItemPosition();
        int rate = binding.rateBar.getNumStars();
        Category category = (Category) binding.spRoom.getItemAtPosition(roomPosition);
        if (!name.isEmpty()) {
            dbHandler.updateFilm(new Film(id, name,"", category.getName(),"" ,rate , category.getId()));
            Toast.makeText(this, this.getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClickAsset(Film film) {
    }

    @Override
    public void deleteAsset(Film film) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getResources().getString(R.string.delete_asset_in_room));
        builder.setMessage(this.getResources().getString(R.string.confirm_delete_asset_in_room));

        builder.setPositiveButton(this.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer roomId = film.getCategoryId();
                film.setCategoryId(0);
                dbHandler.updateFilm(film);
                filmAdapter.submit(dbHandler.getFilmByCategory(roomId));

                updateSpinnerAssetUnknown();
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
}