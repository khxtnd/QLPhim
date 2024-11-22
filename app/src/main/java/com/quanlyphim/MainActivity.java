package com.quanlyphim;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    private boolean isShowRooms = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHandler = new DatabaseHandler(this);
        binding.btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity(Constants.createRoom, null, null);
            }
        });

        binding.btnCreateAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity(Constants.createAsset, null, null);
            }
        });

        binding.btnshowRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllRooms();
            }
        });

        binding.btnShowAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllAssets();
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAssetsMorePrice();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShowRooms) {
            showAllRooms();
        } else {
            showAllAssets();
        }
    }

    @Override
    public void onClickRoom(Category category) {
        moveDetailActivity(Constants.updateRoom, category, null);
    }

    @Override
    public void onClickAsset(Film film) {
        moveDetailActivity(Constants.updateAsset, null, film);
    }

    @Override
    public void deleteAsset(Film film) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getResources().getString(R.string.delete_asset));
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
        builder.setTitle(this.getResources().getString(R.string.delete_room));
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

    private void showAllRooms() {
        isShowRooms=true;
        categoryAdapter = new CategoryAdapter(this);
        binding.rec.setAdapter(categoryAdapter);
        categoryAdapter.submit(dbHandler.getAllCategory());
        binding.tvTitleList.setText(R.string.show_rooms);
    }

    private void showAllAssets() {
        isShowRooms=false;
        filmAdapter = new FilmAdapter(this);
        binding.rec.setAdapter(filmAdapter);
        filmAdapter.submit(dbHandler.getAllFilms());
        binding.tvTitleList.setText(R.string.show_assets);

    }

    private void moveDetailActivity(String valueIntent, Category category, Film film) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.nameIntent, valueIntent);
        if (valueIntent.equals(Constants.updateRoom)) {
            intent.putExtra(Constants.dataIntent, category);
        } else if (valueIntent.equals(Constants.updateAsset)) {
            intent.putExtra(Constants.dataIntent, film);
        }
        startActivity(intent);
    }

    private void showAssetsMorePrice(){
        String price=binding.etPrice.getText().toString();
        if(!price.isEmpty()){
            filmAdapter = new FilmAdapter(this);
            binding.rec.setAdapter(filmAdapter);
            filmAdapter.submit(dbHandler.getAllAssetsMorePrice(Integer.valueOf(price)));
            binding.tvTitleList.setText(R.string.show_assets);
        }else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }

    }
}