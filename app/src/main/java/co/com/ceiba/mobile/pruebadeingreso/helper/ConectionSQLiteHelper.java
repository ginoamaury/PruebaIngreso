package co.com.ceiba.mobile.pruebadeingreso.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConectionSQLiteHelper extends SQLiteOpenHelper {



    public ConectionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Util.CREATE_TABLE_USER);
        db.execSQL(Util.CREATE_TABLE_ADDRESS);
        db.execSQL(Util.CREATE_TABLE_GEO);
        db.execSQL(Util.CREATE_TABLE_COMPANY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS adress");
        db.execSQL("DROP TABLE IF EXISTS geo");
        db.execSQL("DROP TABLE IF EXISTS company");
        onCreate(db);
    }
}