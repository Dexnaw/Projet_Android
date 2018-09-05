package com.arnaud.heh.be.androidproject.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.arnaud.heh.be.androidproject.Database.UserAccessDB;
import com.arnaud.heh.be.androidproject.Models.User;
import com.arnaud.heh.be.androidproject.Models.UserAdapter;

import com.arnaud.heh.be.androidproject.R;

import java.util.ArrayList;

/**
 * Screen with the list of all users.
 *
 * @author Arnaud Urbain
 */
public class UsersActivity extends Activity {

    /**
     * UI references.
     */
    private ListView lv_users_userlist;
    private Toolbar tb_users_toolbar;

    /**
     * DB references.
     */
    private UserAccessDB userDB;
    private ArrayList<User> tabuser;
    private UserAdapter adapter;

    /**
     * Method called at the creation of the activity.
     *
     * @param savedInstanceState save the state of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        lv_users_userlist = findViewById(R.id.lv_users_userlist);
        tb_users_toolbar = findViewById(R.id.tb_users_toolbar);

        setActionBar(tb_users_toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        userDB = new UserAccessDB(this);
        userDB.openForRead();
        tabuser = userDB.getAllUser();
        userDB.close();

        if(tabuser.isEmpty()){
            Toast.makeText(getApplicationContext(), "Error: No user found", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new UserAdapter(this, R.layout.listview_user_row, tabuser);
            lv_users_userlist.setAdapter(adapter);
            lv_users_userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final int id = i;
                    AlertDialog.Builder adb = new AlertDialog.Builder(UsersActivity.this);
                    adb.setTitle("Options");
                    adb.setMessage(tabuser.get(i).getMail().toString());
                    adb.setPositiveButton("Ok", null);
                    adb.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(tabuser.get(id).getRights().toString().equals("admin")) {
                                Toast.makeText(getApplicationContext(), "Error: admin can not be deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                userDB.openForWrite();
                                userDB.removeUser(tabuser.get(id).getMail().toString());
                                userDB.close();
                                adapter.remove(tabuser.get(id));
                                Toast.makeText(getApplicationContext(), "User deleted", Toast.LENGTH_SHORT).show();
                                startActivity(getIntent());
                            }
                        }
                    });
                    adb.setNegativeButton("Modify", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentModifyUser = new Intent(getApplicationContext(), UpdateUserActivity.class);
                            intentModifyUser.putExtra("id", "" + tabuser.get(id).getId());
                            startActivity(intentModifyUser);
                        }
                    });
                    adb.show();
                }
            });
        }
    }
}
