package aan.mrm;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import aan.mrm.fragments.Dialog;

/**
 * Created by Andrey on 29.06.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_TABLE = "messes";
    public static final String DATABASE_TABLE_LOGS = "messes_logs";
    public static final String MESS_COLUMN = "mess";
    public static final String MESS_COLUMN_LOGS = "mess_user";
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID="id";
    private static final String DATABASE_NAME = "messesdb.db";
    public static final String APP_PREFERENCES = "logandpass";
    public SharedPreferences logandpass;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "
                + DATABASE_TABLE + "_" + Dialog.app_user + " (" + COLUMN_ID
                + " integer primary key autoincrement, " + MESS_COLUMN
                + " text not null);");
        db.execSQL("create table "
                + DATABASE_TABLE_LOGS + " (" + COLUMN_ID
                + " integer primary key autoincrement, " + MESS_COLUMN_LOGS
                + " text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
