package com.arnaud.heh.be.androidproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arnaud.heh.be.androidproject.Models.Automaton;
import java.util.ArrayList;

/**
 * Automaton table access.
 *
 * @author Arnaud Urbain
 */

public class AutomatonAccessDB {

    /**
     * Database references.
     */
    private static final int VERSION = 1;
    private static final String NAME_DB = "Automaton.db";
    private SQLiteDatabase db;
    private DatabaseSQLite automatonDB;

    /**
     * Table content.
     */
    private static final String TABLE_AUTOMATON = "TABLE_AUTOMATON";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NAME = "NAME";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_TYPE = "TYPE";
    private static final int NUM_COL_TYPE = 2;
    private static final String COL_RACKNUMBER = "RACKNUMBER";
    private static final int NUM_COL_RACKNUMBER = 3;
    private static final String COL_SLOTNUMBER = "SLOTNUMBER";
    private static final int NUM_COL_SLOTNUMBER = 4;

    /**
     * Constructor.
     *
     * @param c context of the activity
     */
    public AutomatonAccessDB(Context c){
        automatonDB = new DatabaseSQLite(c, NAME_DB, null, VERSION);
    }

    /**
     * Opens the database to write in it.
     */
    public void openForWrite(){
        db = automatonDB.getWritableDatabase();
    }

    /**
     * Opens the database to read in it.
     */
    public void openForRead(){
        db = automatonDB.getReadableDatabase();
    }

    /**
     * Closes the database.
     */
    public void close(){
        db.close();
    }

    /**
     * Inserts an automaton in the database.
     *
     * @param a new automaton
     * @return insertion of the automaton
     */
    public long insertAutomaton(Automaton a){
        ContentValues content = new ContentValues();
        content.put(COL_NAME, a.getName());
        content.put(COL_TYPE, a.getType());
        content.put(COL_RACKNUMBER, a.getRackNumber());
        content.put(COL_SLOTNUMBER, a.getSlotNumber());

        return db.insert(TABLE_AUTOMATON, null, content);
    }

    /**
     * Updates an automaton in the database.
     *
     * @param id id of the automaton
     * @param a automaton with updates
     * @return update of the automaton
     */
    public int updateAutomaton(int id, Automaton a){
        ContentValues content = new ContentValues();
        content.put(COL_NAME, a.getName());
        content.put(COL_TYPE, a.getType());
        content.put(COL_RACKNUMBER, a.getRackNumber());
        content.put(COL_SLOTNUMBER, a.getSlotNumber());

        return db.update(TABLE_AUTOMATON, content, COL_ID + " = " + id, null);
    }

    /**
     * Removes an automaton from the database.
     *
     * @param id id of the automaton
     * @return delete the automaton
     */
    public int removeAutomaton(int id){
        return db.delete(TABLE_AUTOMATON, COL_ID + " = " + id, null);
    }

    /**
     * Truncates the automaton table.
     *
     * @return delete the table
     */
    public int truncateAutomatons(){
        return db.delete(TABLE_AUTOMATON, null, null);
    }

    /**
     * Gets the automaton by id.
     *
     * @param id id of the automaton
     * @return automaton retrieved
     */
    public Automaton getAutomatonById(int id){
        Cursor c = db.query(TABLE_AUTOMATON,
                new String[] {COL_ID, COL_NAME, COL_TYPE, COL_RACKNUMBER, COL_SLOTNUMBER},
                COL_ID + " LIKE \"" + id + "\"",
                null, null, null, COL_TYPE);
        return cursorToAutomaton(c);
    }

    /**
     * Place the cursor on the automaton.
     *
     * @param c cursor
     * @return automaton selected
     */
    public Automaton cursorToAutomaton (Cursor c){
        if(c.getCount() == 0){
            c.close();
            return null;
        }
        c.moveToFirst();
        Automaton a = new Automaton();
        a.setId(c.getInt(NUM_COL_ID));
        a.setName(c.getString(NUM_COL_NAME));
        a.setType(c.getString(NUM_COL_TYPE));
        a.setRackNumber(c.getInt(NUM_COL_RACKNUMBER));
        a.setSlotNumber(c.getInt(NUM_COL_SLOTNUMBER));
        c.close();
        return a;
    }

    /**
     * Gets all users in the table.
     *
     * @return list with all users
     */
    public ArrayList<Automaton> getAllAutomaton(){
        Cursor c = db.query(TABLE_AUTOMATON,
                new String[] {COL_ID, COL_NAME, COL_TYPE, COL_RACKNUMBER, COL_SLOTNUMBER},
                null, null, null, null, COL_TYPE);
        ArrayList<Automaton> tabAutomaton = new ArrayList<Automaton>();
        if(c.getCount() == 0) {
            c.close();
            return tabAutomaton;
        }
        while(c.moveToNext()){
            Automaton user = new Automaton();
            user.setId(c.getInt(NUM_COL_ID));
            user.setName(c.getString(NUM_COL_NAME));
            user.setType(c.getString(NUM_COL_TYPE));
            user.setRackNumber(c.getInt(NUM_COL_RACKNUMBER));
            user.setSlotNumber(c.getInt(NUM_COL_SLOTNUMBER));
            tabAutomaton.add(user);
        }
        c.close();
        return tabAutomaton;
    }

}
