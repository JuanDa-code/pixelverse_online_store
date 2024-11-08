package com.pixelverse.onlinestore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "tienda.db";

    public static final String TABLE_USUARIOS = "t_usuarios";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "usuario";
    public static final String COLUMN_CONTRASENA = "contrasena";

    public static final String TABLE_PRODUCTOS = "t_productos";
    public static final String COLUMN_ID_PRODUCTO = "id_producto";
    public static final String COLUMN_NOMBRE_PRODUCTO = "nombre";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_IMAGEN_RES_ID = "imagen_id";
    public static final String COLUMN_IMAGEN_URL = "imagen_url";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_USUARIOS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT NOT NULL," +
                COLUMN_CONTRASENA + " TEXT NOT NULL)";
        db.execSQL(sql);

        String sql1 = "CREATE TABLE " + TABLE_PRODUCTOS + "(" +
                COLUMN_ID_PRODUCTO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOMBRE_PRODUCTO + " TEXT NOT NULL," +
                COLUMN_PRECIO + " DOUBLE NOT NULL," +
                COLUMN_IMAGEN_RES_ID + " INTEGER," +
                COLUMN_IMAGEN_URL + " TEXT)";
        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);

        onCreate(db);
    }
}