package com.example.chatapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VTBaglanti extends SQLiteOpenHelper {
    private static final String VT="database";
    public VTBaglanti(Context context) {
        super(context, VT, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(id INTEGER Primary Key AUTOINCREMENT, isim TEXT, email TEXT UNIQUE, parola TEXT, fotograf TEXT)");
        db.execSQL("CREATE TABLE message(id INTEGER Primary Key AUTOINCREMENT, gönderen TEXT, alan TEXT, mesaj TEXT)");
        db.execSQL("CREATE TABLE friend(ekleyen TEXT, eklenen TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS message");
        db.execSQL("DROP TABLE IF EXISTS friend");
        onCreate(db);
        onCreate(db);
        onCreate(db);
    }
}
