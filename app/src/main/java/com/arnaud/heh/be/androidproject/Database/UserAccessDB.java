package com.arnaud.heh.be.androidproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arnaud.heh.be.androidproject.Models.User;
import java.util.ArrayList;

/**
 * User table access.
 *
 * @author Arnaud Urbain
 */
public class UserAccessDB {

    /**
     * Database references.
     */
    private static final int VERSION = 1;
    private static final String NAME_DB = "User.db";
    private SQLiteDatabase db;
    private DatabaseSQLite userDB;

    /**
     * Table content.
     */
    private static final String TABLE_USER = "TABLE_USER";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_FIRSTNAME = "FIRSTNAME";
    private static final int NUM_COL_FIRSTNAME = 1;
    private static final String COL_NAME = "NAME";
    private static final int NUM_COL_NAME = 2;
    private static final String COL_EMAIL = "EMAIL";
    private static final int NUM_COL_EMAIL = 3;
    private static final String COL_PASSWORD = "PASSWORD";
    private static final int NUM_COL_PASSWORD = 4;
    private static final String COL_RIGHTS = "RIGHTS";
    private static final int NUM_COL_RIGHTS = 5;

    /**
     * Constructor.
     *
     * @param c context of the activity
     */
    public UserAccessDB(Context c){
        userDB = new DatabaseSQLite(c, NAME_DB, null, VERSION);
    }

    /**
     * Opens the database to write in it.
     */
    public void openForWrite(){
        db = userDB.getWritableDatabase();
    }

    /**
     * Opens the database to read in it.
     */
    public void openForRead(){
        db = userDB.getReadableDatabase();
    }

    /**
     * Closes the database.
     */
    public void close(){
        db.close();
    }

    /**
     * Inserts an user in the database.
     *
     * @param u new user
     * @return insertion of the user
     */
    public long insertUser(User u){
        ContentValues content = new ContentValues();
        content.put(COL_FIRSTNAME, u.getFirstName());
        content.put(COL_NAME, u.getName());
        content.put(COL_EMAIL, u.getMail());
        content.put(COL_PASSWORD, u.getPwd());
        content.put(COL_RIGHTS, u.getRights());

        return db.insert(TABLE_USER, null, content);
    }

    /**
     * Updates an user in the database.
     *
     * @param id id of the user
     * @param u user with updates
     * @return update of the user
     */
    public int updateUser(int id, User u){
        ContentValues content = new ContentValues();
        content.put(COL_FIRSTNAME, u.getFirstName());
        content.put(COL_NAME, u.getName());
        content.put(COL_EMAIL, u.getMail());
        content.put(COL_PASSWORD, u.getPwd());
        content.put(COL_RIGHTS, u.getRights());

        return db.update(TABLE_USER, content, COL_ID + " = " + id, null);
    }

    /**
     * Removes an user from the database.
     *
     * @param mail mail of the user
     * @return delete the user
     */
    public int removeUser(String mail){
        return db.delete(TABLE_USER, COL_EMAIL + " = ?",  new String[] {mail});
    }

    /**
     * Truncates the user table.
     *
     * @return delete the table
     */
    public int truncateUsers(){
        return db.delete(TABLE_USER, null, null);
    }

    /**
     * Gets the user by mail.
     *
     * @param mail mail of the user
     * @return user retrieved
     */
    public User getUserByMail(String mail){
        Cursor c = db.query(TABLE_USER,
                new String[] {COL_ID, COL_FIRSTNAME, COL_NAME, COL_EMAIL, COL_PASSWORD, COL_RIGHTS},
                COL_EMAIL + " LIKE \"" + mail + "\"",
                null, null, null, COL_EMAIL);
        return cursorToUser(c);
    }

    /**
     * Gets the user by id.
     *
     * @param id id of the user
     * @return user retrieved
     */
    public User getUserById(int id){
        Cursor c = db.query(TABLE_USER,
                new String[] {COL_ID, COL_FIRSTNAME, COL_NAME, COL_EMAIL, COL_PASSWORD, COL_RIGHTS},
                COL_ID + " LIKE \"" + id + "\"",
                null, null, null, COL_EMAIL);
        return cursorToUser(c);
    }

    /**
     * Place the cursor on the user.
     *
     * @param c cursor
     * @return user selected
     */
    public User cursorToUser(Cursor c){
        if(c.getCount() == 0){
            c.close();
            return null;
        }
        c.moveToFirst();
        User user = new User();
        user.setId(c.getInt(NUM_COL_ID));
        user.setFirstName(c.getString(NUM_COL_FIRSTNAME));
        user.setName(c.getString(NUM_COL_NAME));
        user.setMail(c.getString(NUM_COL_EMAIL));
        user.setPwd(c.getString(NUM_COL_PASSWORD));
        user.setRights(c.getString(NUM_COL_RIGHTS));
        c.close();
        return user;
    }

    /**
     * Gets all users in the table.
     *
     * @return list with all users
     */
    public ArrayList<User> getAllUser(){
        Cursor c = db.query(TABLE_USER,
                new String[] {COL_ID, COL_FIRSTNAME, COL_NAME, COL_EMAIL, COL_PASSWORD, COL_RIGHTS},
                null, null, null, null, COL_EMAIL);
        ArrayList<User> tabUser = new ArrayList<User>();
        if(c.getCount() == 0) {
            c.close();
            return tabUser;
        }
        while(c.moveToNext()){
            User user = new User();
            user.setId(c.getInt(NUM_COL_ID));
            user.setFirstName(c.getString(NUM_COL_FIRSTNAME));
            user.setName(c.getString(NUM_COL_NAME));
            user.setMail(c.getString(NUM_COL_EMAIL));
            user.setPwd(c.getString(NUM_COL_PASSWORD));
            user.setRights(c.getString(NUM_COL_RIGHTS));
            tabUser.add(user);
        }
        c.close();
        return tabUser;
    }

}
