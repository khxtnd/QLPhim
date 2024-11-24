package com.quanlyphim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.quanlyphim.model.Film;
import com.quanlyphim.model.Category;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "qlfilm.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_FILM = "film";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_EVALUATE = "evaluate";
    private static final String KEY_CATEGORY_ID = "categoryId";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoryTableQuery = "CREATE TABLE " + TABLE_CATEGORY + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_DESC + " TEXT" +
                ")";
        db.execSQL(createCategoryTableQuery);

        String createFilmTableQuery = "CREATE TABLE " + TABLE_FILM + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_CATEGORY + " TEXT," +
                KEY_DESC + " TEXT," +
                KEY_IMAGE + " TEXT," +
                KEY_EVALUATE + " INTEGER," +
                KEY_CATEGORY_ID + " INTEGER," +
                "FOREIGN KEY(" + KEY_CATEGORY_ID + ") REFERENCES " +
                TABLE_CATEGORY + "(" + KEY_ID + ")" +
                ")";
        db.execSQL(createFilmTableQuery);

        if (isTableEmpty(db, TABLE_CATEGORY)) {
            insertDefaultCategories(db);
        }

        if (isTableEmpty(db, TABLE_FILM)) {
            insertDefaultFilms(db);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    private boolean isTableEmpty(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_DESC + ") VALUES " +
                "(0, 'Không xác định', ''), " +
                "(1, 'Tất cả', ''), " +
                "(2, 'Action', 'Movies with exciting action sequences'), " +
                "(3, 'Comedy', 'Funny and entertaining movies'), " +
                "(4, 'Drama', 'Emotionally powerful movies')");
    }

    private void insertDefaultFilms(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_FILM + " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_CATEGORY + ", " +
                KEY_IMAGE + ", " + KEY_EVALUATE + ", " + KEY_CATEGORY_ID + ") VALUES " +
                "(1, 'Die Hard', 'Action', 'die_hard.jpg', 5, 2), " +
                "(2, 'The Mask', 'Comedy', 'the_mask.jpg', 4, 3), " +
                "(3, 'The Pursuit of Happyness', 'Drama', 'pursuit_of_happyness.jpg', 3, 4)");
    }

    public void insertCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());
        values.put(KEY_DESC, category.getDesc());
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    public void insertFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, film.getName());
        values.put(KEY_CATEGORY, film.getCategory());
        values.put(KEY_IMAGE, film.getImage());
        values.put(KEY_EVALUATE, film.getEvaluate());
        values.put(KEY_CATEGORY_ID, film.getCategoryId());
        db.insert(TABLE_FILM, null, values);
        db.close();
    }

    public List<Category> getAllCategory() {
        List<Category> categoryList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            categoryList.add(category);
            cursor.moveToNext();
        }
        return categoryList;
    }

    public List<Film> getAllFilms() {
        List<Film> filmList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            Film film = new Film(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            filmList.add(film);
            cursor.moveToNext();
        }
        return filmList;
    }

    public List<Film> getFilmByCategory(Integer categoryId) {
        List<Film> filmList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM + " WHERE " + KEY_CATEGORY_ID + " = " + categoryId + ";";
        ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            Film film = new Film(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            filmList.add(film);
            cursor.moveToNext();
        }
        return filmList;
    }

    public void updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());
        values.put(KEY_DESC, category.getDesc());
        db.update(TABLE_CATEGORY, values, KEY_ID + " = ?", new String[]{String.valueOf(category.getId())});
        db.close();
    }

    public void updateFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, film.getName());
        values.put(KEY_CATEGORY, film.getCategory());
        values.put(KEY_IMAGE, film.getImage());
        values.put(KEY_EVALUATE, film.getEvaluate());
        values.put(KEY_CATEGORY_ID, film.getCategoryId());
        db.update(TABLE_FILM, values, KEY_ID + " = ?", new String[]{String.valueOf(film.getId())});
        db.close();
    }


    public void deleteCategory(int cateId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, KEY_ID + " = ?", new String[]{String.valueOf(cateId)});
        db.close();
    }

    public void deleteFilm(int filmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID + " = ?", new String[]{String.valueOf(filmId)});
        db.close();
    }

    public List<Film> getAllFilmStarHot(Integer star) {
        List<Film> filmList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM + " WHERE " + KEY_EVALUATE + " >= " + star + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            Film film = new Film(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            filmList.add(film);
            cursor.moveToNext();
        }
        return filmList;
    }

    public List<Film> getAllFilmUnknownCate() {
        List<Film> filmList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM + " WHERE " + KEY_CATEGORY_ID + " = 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            Film film = new Film(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            filmList.add(film);
            cursor.moveToNext();
        }
        return filmList;
    }
}
