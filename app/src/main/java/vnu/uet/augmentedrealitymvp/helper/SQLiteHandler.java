package vnu.uet.augmentedrealitymvp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vnu.uet.augmentedrealitymvp.model.Marker;

/**
 * Created by hienbx94 on 3/21/16.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "android_api";

    private static final String TABLE_MARKER_ONLINE = "marker";
    private static final String TABLE_MARKER_OFFLINE = "MarkerOffline";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_ISET = "iset";
    private static final String KEY_FSET = "fset";
    private static final String KEY_FSET3 = "fset3";
    private static final String KEY_STT = "stt";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MARKER_ONLINE_TABLE = "CREATE TABLE " + TABLE_MARKER_ONLINE + "("
                + KEY_ID + " INTEGER," + KEY_STT + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " TEXT UNIQUE," + KEY_ISET + " TEXT," + KEY_FSET3 + " TEXT,"
                + KEY_FSET + " TEXT" + ")";
        db.execSQL(CREATE_MARKER_ONLINE_TABLE);

        String CREATE_MARKER_OFFLINE_TABLE = "CREATE TABLE " + TABLE_MARKER_OFFLINE + "("
                + KEY_ID + " INTEGER," + KEY_STT + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " TEXT UNIQUE," + KEY_ISET + " TEXT," + KEY_FSET3 + " TEXT,"
                + KEY_FSET + " TEXT" + ")";
        db.execSQL(CREATE_MARKER_OFFLINE_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKER_ONLINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKER_OFFLINE);
        // Create tables again
        onCreate(db);
    }

    public void addMarkerOnline(Marker marker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, marker.get_id());
        values.put(KEY_NAME, marker.get_name());
        values.put(KEY_IMAGE, marker.get_image());
        values.put(KEY_ISET, marker.get_iset());
        values.put(KEY_FSET, marker.get_fset());
        values.put(KEY_FSET3, marker.get_fset3());

        db.insert(TABLE_MARKER_ONLINE, null, values);
        db.close();
    }

    public void addMarkerOffline(Marker marker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, marker.get_id());
        values.put(KEY_NAME, marker.get_name());
        values.put(KEY_IMAGE, marker.get_image());
        values.put(KEY_ISET, marker.get_iset());
        values.put(KEY_FSET, marker.get_fset());
        values.put(KEY_FSET3, marker.get_fset3());

        db.insert(TABLE_MARKER_OFFLINE, null, values);
        db.close();
    }


    public List<Marker> getAllMarkersOnline() {
        List<Marker> markerList = new ArrayList<Marker>();

        String selectQuery = "SELECT  * FROM " + TABLE_MARKER_ONLINE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Marker marker = new Marker();
                marker.set_id(Integer.parseInt(cursor.getString(0)));
                marker.set_name(cursor.getString(2));
                marker.set_image(cursor.getString(3));
                marker.set_iset(cursor.getString(4));
                marker.set_fset3(cursor.getString(5));
                marker.set_fset(cursor.getString(6));
                markerList.add(marker);
            } while (cursor.moveToNext());
        }
        return markerList;
    }

    public List<Marker> getAllMarkersOffline() {
        List<Marker> markerList = new ArrayList<Marker>();

        String selectQuery = "SELECT  * FROM " + TABLE_MARKER_OFFLINE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Marker marker = new Marker();
                marker.set_id(Integer.parseInt(cursor.getString(0)));
                marker.set_name(cursor.getString(2));
                marker.set_image(cursor.getString(3));
                marker.set_iset(cursor.getString(4));
                marker.set_fset3(cursor.getString(5));
                marker.set_fset(cursor.getString(6));
                markerList.add(marker);
            } while (cursor.moveToNext());
        }
        return markerList;
    }

    public boolean deleteAllMarkersOnline() {
        String query = "DELETE FROM " + TABLE_MARKER_ONLINE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        return true;
    }

    public boolean deleteMarkersOnline(int id) {
        String query = "DELETE FROM " + TABLE_MARKER_ONLINE + " WHERE " + KEY_ID + " = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        return true;
    }

    public boolean deleteAllMarkersOffline() {
        String query = "DELETE FROM " + TABLE_MARKER_OFFLINE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        return true;
    }
}