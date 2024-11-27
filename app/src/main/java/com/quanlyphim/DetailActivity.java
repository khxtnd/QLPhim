package com.quanlyphim;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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

    private String linkImage = "";

    private static final int PICK_IMAGE_REQUEST = 1;

    private Film filmSelected=new Film();
    private Category categoryUpdate=new Category();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());

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
        binding.btnSelectFile.setOnClickListener(v -> {
            openImagePicker();
        });

        binding.btnDeleteFilmInCate.setOnClickListener(v -> {
            Film film=filmSelected;
            film.setCategoryId(0);
            film.setCategory("Không xác định");
            dbHandler.updateFilm(film);

            allFilmUnknownCate = dbHandler.getAllFilmUnknownCate();
            binding.linearAddFilmInCate.setVisibility(View.VISIBLE);
            ArrayAdapter<Film> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allFilmUnknownCate);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spFilm.setAdapter(adapter);

            filmAdapter.submit(dbHandler.getFilmByCategory(categoryUpdate.getId()));
        });
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
        binding.spCategory.setAdapter(adapter);
        binding.spCategory.setSelection(0);

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
        String desc = binding.etDescFilm.getText().toString();

        int star = (int) binding.ratingBar.getRating();

        int catePosition = binding.spCategory.getSelectedItemPosition();
        Category cate = (Category) binding.spCategory.getItemAtPosition(catePosition);
        if (!name.isEmpty() && !desc.isEmpty()) {
            dbHandler.insertFilm(new Film(name, cate.getName(), desc, linkImage, star, cate.getId()));
            Toast.makeText(this, this.getResources().getString(R.string.create_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }

    private void showViewUpdateCate() {
        binding.tvBanner.setText(this.getResources().getText(R.string.update_or_delete_category));
        binding.btnAddOrUpdate.setText(this.getResources().getText(R.string.update));

        Category category = (Category) getIntent().getSerializableExtra(Constants.dataIntent);
        categoryUpdate=category;
        binding.etNameCate.setText(category.getName());
        binding.etDescCate.setText(category.getDesc());
        binding.btnDelete.setVisibility(View.VISIBLE);

        filmAdapter = new FilmAdapter(DetailActivity.this, this);
        binding.recFilm.setAdapter(filmAdapter);
        filmAdapter.submit(dbHandler.getFilmByCategory(category.getId()));

        allFilmUnknownCate = dbHandler.getAllFilmUnknownCate();

        if (!allFilmUnknownCate.isEmpty()) {
            binding.linearAddFilmInCate.setVisibility(View.VISIBLE);
            ArrayAdapter<Film> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allFilmUnknownCate);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spFilm.setAdapter(adapter);
        }

        ArrayAdapter<Film> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allFilmUnknownCate);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spFilm.setAdapter(adapter);

        binding.btnAddOrUpdateFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int filmPosition = binding.spFilm.getSelectedItemPosition();
                Film film = (Film) binding.spFilm.getItemAtPosition(filmPosition);
                if (film != null && film.getId() != 0) {
                    film.setCategoryId(category.getId());
                    film.setCategory(category.getName());
                    dbHandler.updateFilm(film);
                    filmAdapter.submit(dbHandler.getFilmByCategory(category.getId()));
                    updateSpinnerFilmNoCate();
                }

            }
        });
        binding.btnAddOrUpdate.setOnClickListener(v -> updateCate(category.getId()));

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle(DetailActivity.this.getResources().getString(R.string.delete_category));
                builder.setMessage(DetailActivity.this.getResources().getString(R.string.confirm_delete));

                builder.setPositiveButton(DetailActivity.this.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.deleteCategory(category.getId());
                        finish();
                    }
                });

                builder.setNegativeButton(DetailActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void updateSpinnerFilmNoCate() {
        allFilmUnknownCate = dbHandler.getAllFilmUnknownCate();
        if (!allFilmUnknownCate.isEmpty()) {
            binding.linearAddFilmInCate.setVisibility(View.VISIBLE);
            ArrayAdapter<Film> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allFilmUnknownCate);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spFilm.setAdapter(adapter);
        }else {
            binding.linearAddFilmInCate.setVisibility(View.GONE);
        }

        Toast.makeText(this, this.getResources().getString(R.string.add_film_success), Toast.LENGTH_SHORT).show();

    }

    private void updateCate(Integer id) {
        String name = binding.etNameCate.getText().toString();
        String desc = binding.etDescCate.getText().toString();

        if (!name.isEmpty() && !desc.isEmpty()) {
            dbHandler.updateCategory(new Category(id, name, desc));
            List<Film> listFilmInCate = dbHandler.getFilmByCategory(id);
            for (Film film : listFilmInCate) {
                film.setCategory(name);
                dbHandler.updateFilm(film);
            }
            Toast.makeText(this, this.getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }

    private void showViewUpdateFilm() {
        allCategories = dbHandler.getAllCategory();
        allCategories.remove(0);
        allCategories.remove(0);

        binding.tvBanner.setText(this.getResources().getText(R.string.update_or_delete_film));

        binding.linearCate.setVisibility(View.GONE);
        binding.linearFilm.setVisibility(View.VISIBLE);
        binding.btnDelete.setVisibility(View.VISIBLE);
        binding.btnAddOrUpdate.setText(this.getResources().getText(R.string.update));

        Film film = (Film) getIntent().getSerializableExtra(Constants.dataIntent);
        if (film != null) {
            binding.etNameFilm.setText(film.getName());
            binding.ratingBar.setRating(film.getEvaluate());
        }



        int catePosition = 0;
        for (int i = 0; i < allCategories.size(); i++) {
            if (film != null && film.getCategoryId().equals(allCategories.get(i).getId())) {
                catePosition= i;
            }
        }

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spCategory.setAdapter(adapter);
        Log.e("khanhpq", catePosition+"");
        binding.spCategory.setSelection(catePosition);

        if (film.getCategoryId() == 0) {
            binding.spCategory.setSelection(0);
        } else {
            binding.spCategory.setSelection(catePosition);
        }

        Glide.with(this)
                .load(film.getImage())
                .placeholder(R.drawable.img_star_war)
                .error(R.drawable.img_star_war)
                .into(binding.ivFilm);
        binding.btnAddOrUpdate.setOnClickListener(v -> updateFilm(film.getId()));


        binding.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
            builder.setTitle(DetailActivity.this.getResources().getString(R.string.delete_film));
            builder.setMessage(DetailActivity.this.getResources().getString(R.string.confirm_delete));

            builder.setPositiveButton(DetailActivity.this.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbHandler.deleteFilm(film.getId());
                    finish();
                }
            });

            builder.setNegativeButton(DetailActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void updateFilm(Integer id) {
        String name = binding.etNameFilm.getText().toString();
        String desc = binding.etDescFilm.getText().toString();
        int catePosition = binding.spCategory.getSelectedItemPosition();
        int evaluate = (int) binding.ratingBar.getRating();
        Category category = (Category) binding.spCategory.getItemAtPosition(catePosition);
        if (!name.isEmpty()) {
            dbHandler.updateFilm(new Film(id, name, category.getName(), desc, linkImage, evaluate, category.getId()));
            Toast.makeText(this, this.getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClickFilm(Film film) {
        filmSelected=film;
        binding.btnDeleteFilmInCate.setVisibility(View.VISIBLE);
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.img_star_war)
                        .error(R.drawable.img_star_war)
                        .into(binding.ivFilm);
                linkImage = imageUri.toString();
            } else {
                Toast.makeText(this, "Không chọn được hình ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

}