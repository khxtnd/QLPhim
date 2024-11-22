package com.quanlyphim;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

    private List<Film> allAssetsUnknown;

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
            case Constants.createRoom:
                showViewCreateRoom();
                break;
            case Constants.createAsset:
                showViewCreateAsset();
                break;
            case Constants.updateRoom:
                showViewUpdateRoom();
                break;
            default:
                showViewUpdateAsset();
                break;
        }


        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showViewCreateRoom() {
        binding.tvBanner.setText(this.getResources().getText(R.string.create_room));

        binding.btnCOrU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom();
            }
        });
    }

    private void createRoom() {
        String name = binding.etNameRoom.getText().toString();
        String desc = binding.etDescRoom.getText().toString();

        if (!name.isEmpty() && !desc.isEmpty()) {
            dbHandler.insertCategory(new Category(name, desc));
            Toast.makeText(this, this.getResources().getString(R.string.create_success), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }

    private void showViewCreateAsset() {
        binding.tvBanner.setText(this.getResources().getText(R.string.create_asset));
        binding.ctRoom.setVisibility(View.GONE);
        binding.ctAsset.setVisibility(View.VISIBLE);

        allCategories = dbHandler.getAllCategory();
        allCategories.add(0,new Category(0, this.getResources().getString(R.string.no_in_room), ""));

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spRoom.setAdapter(adapter);
        binding.spRoom.setSelection(0);

        binding.btnCOrU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAsset();
                finish();
            }
        });
    }

    private void createAsset() {
        String name = binding.etNameAsset.getText().toString();
        String cate = binding.etCateAsset.getText().toString();
        String position = binding.etPosition.getText().toString();
        String price = binding.etPrice.getText().toString();
        int roomPosition = binding.spRoom.getSelectedItemPosition();

        if (!name.isEmpty() && !cate.isEmpty() && !position.isEmpty() && !price.isEmpty()) {
            if (roomPosition == 0) {
                dbHandler.insertFilm(new Film(name, cate, position, Integer.valueOf(price), 0));
            } else {
                List<Category> allCategories = dbHandler.getAllCategory();
                Integer roomId = allCategories.get(roomPosition - 1).getId();
                dbHandler.insertFilm(new Film(name, cate, position, Integer.valueOf(price), roomId));
            }
            Toast.makeText(this, this.getResources().getString(R.string.create_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }

    private void showViewUpdateRoom() {
        binding.tvBanner.setText(this.getResources().getText(R.string.update_room));
        binding.btnCOrU.setText(this.getResources().getText(R.string.update));

        Category category = (Category) getIntent().getSerializableExtra(Constants.dataIntent);
        binding.etNameRoom.setText(category.getName());
        binding.etDescRoom.setText(category.getDesc());

        binding.ctAddAssetInRoom.setVisibility(View.VISIBLE);
        filmAdapter = new FilmAdapter(this);
        binding.rec.setAdapter(filmAdapter);
        filmAdapter.submit(dbHandler.getFilmByCategory(category.getId()));

        allAssetsUnknown = dbHandler.getAllAssetsUnknown();
        allAssetsUnknown.add(0, new Film(0, this.getResources().getString(R.string.unknonwn), "", "", 0, 0));

        ArrayAdapter<Film> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allAssetsUnknown);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spAsset.setAdapter(adapter);

        binding.btnAddAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int assetPosition = binding.spAsset.getSelectedItemPosition();
                Film film = (Film) binding.spAsset.getItemAtPosition(assetPosition);
                if (film != null && film.getId() != 0) {
                    film.setCategoryId(category.getId());
                    dbHandler.updateFilm(film);
                    filmAdapter.submit(dbHandler.getFilmByCategory(category.getId()));
                    updateSpinnerAssetUnknown();
                }

            }
        });
        binding.btnCOrU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRoom(category.getId());
            }
        });
    }

    private void updateSpinnerAssetUnknown() {
        allAssetsUnknown = dbHandler.getAllAssetsUnknown();
        allAssetsUnknown.add(0, new Film(0, this.getResources().getString(R.string.unknonwn), "", "", 0, 0));

        ArrayAdapter<Film> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allAssetsUnknown);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spAsset.setAdapter(adapter);
        Toast.makeText(this, this.getResources().getString(R.string.add_asset_success), Toast.LENGTH_SHORT).show();

    }

    private void updateRoom(Integer id) {
        String name = binding.etNameRoom.getText().toString();
        String desc = binding.etDescRoom.getText().toString();

        if (!name.isEmpty() && !desc.isEmpty()) {
            dbHandler.updateCategory(new Category(id, name, desc));
            Toast.makeText(this, this.getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }

    private void showViewUpdateAsset() {
        binding.tvBanner.setText(this.getResources().getText(R.string.update_asset));

        binding.ctRoom.setVisibility(View.GONE);
        binding.ctAsset.setVisibility(View.VISIBLE);
        binding.btnCOrU.setText(this.getResources().getText(R.string.update));

        Film film = (Film) getIntent().getSerializableExtra(Constants.dataIntent);
        if (film != null) {
            binding.etNameAsset.setText(film.getName());
            binding.etCateAsset.setText(film.getCategory());
            binding.etPosition.setText(film.getImage());
            binding.etPrice.setText(film.getRate().toString());
        }

        allCategories = dbHandler.getAllCategory();
        Integer roomPosition = 0;
        allCategories.add(0,new Category(0, this.getResources().getString(R.string.no_in_room), ""));
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
        binding.btnCOrU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAsset(film.getId());
            }
        });
    }

    private void updateAsset(Integer id) {
        String name = binding.etNameAsset.getText().toString();
        String cate = binding.etCateAsset.getText().toString();
        String position = binding.etPosition.getText().toString();
        String price = binding.etPrice.getText().toString();
        int roomPosition = binding.spRoom.getSelectedItemPosition();
        Category category = (Category) binding.spRoom.getItemAtPosition(roomPosition);
        Log.e("khanhpq", category.getName());
        if (!name.isEmpty() && !cate.isEmpty() && !position.isEmpty() && !price.isEmpty()) {
            dbHandler.updateFilm(new Film(id, name, cate, position, Integer.valueOf(price), category.getId()));
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