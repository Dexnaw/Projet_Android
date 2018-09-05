package com.arnaud.heh.be.androidproject.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creation of the database.
 *
 * @author Arnaud Urbain
 */
public class DatabaseSQLite extends SQLiteOpenHelper {

    /**
     * Table user content.
     */
    private static final String TABLE_USER = "TABLE_USER";
    private static final String USER_COL_ID = "ID";
    private static final String USER_COL_FIRSTNAME = "FIRSTNAME";
    private static final String USER_COL_NAME = "NAME";
    private static final String USER_COL_EMAIL = "EMAIL";
    private static final String USER_COL_PASSWORD = "PASSWORD";
    private static final String USER_COL_RIGHTS = "RIGHTS";

    /**
     * Table automaton content.
     */
    private static final String TABLE_AUTOMATON = "TABLE_AUTOMATON";
    private static final String AUTOMATON_COL_ID = "ID";
    private static final String AUTOMATON_COL_NAME = "NAME";
    private static final String AUTOMATON_COL_TYPE = "TYPE";
    private static final String AUTOMATON_COL_RACKNUMBER = "RACKNUMBER";
    private static final String AUTOMATON_COL_SLOTNUMBER = "SLOTNUMBER";

    /**
     * Database creation queries.
     */
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " ("
            + USER_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_COL_FIRSTNAME + " TEXT NOT NULL, "
            + USER_COL_NAME + " TEXT NOT NULL, "
            + USER_COL_EMAIL + " TEXT NOT NULL UNIQUE, "
            + USER_COL_PASSWORD + " TEXT NOT NULL, "
            + USER_COL_RIGHTS + " TEXT NOT NULL);";
    private static final String CREATE_AUTOMATON_TABLE = "CREATE TABLE " + TABLE_AUTOMATON + " ("
            + AUTOMATON_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AUTOMATON_COL_NAME + " TEXT NOT NULL, "
            + AUTOMATON_COL_TYPE + " TEXT NOT NULL, "
            + AUTOMATON_COL_RACKNUMBER + " INTEGER NOT NULL, "
            + AUTOMATON_COL_SLOTNUMBER + " INTEGER NOT NULL);";

    /**
     * Constructor.
     *
     * @param context context of the activity
     * @param name name of the database
     * @param factory sub-classes of cursor when calling query
     * @param version version of the database
     */
    public DatabaseSQLite(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }

    /**
     * Creation of the database.
     *
     * @param db database
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_AUTOMATON_TABLE);
    }

    /**
     * Upgrade of the database.
     *
     * @param db database
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE " + TABLE_USER);
        db.execSQL("DROP TABLE " + TABLE_AUTOMATON);
        onCreate(db);
    }

}