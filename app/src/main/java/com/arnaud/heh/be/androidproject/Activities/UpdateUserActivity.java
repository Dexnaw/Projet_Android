package com.arnaud.heh.be.androidproject.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.arnaud.heh.be.androidproject.Database.UserAccessDB;
import com.arnaud.heh.be.androidproject.Models.User;
import com.arnaud.heh.be.androidproject.R;

import java.security.NoSuchAlgorithmException;

import static com.arnaud.heh.be.androidproject.Models.HashPassword.hashPassword;

/**
 * Modification of users screen.
 *
 * @author Arnaud Urbain
 */
public class UpdateUserActivity extends Activity {

    /**
     * UI references.
     */
    private EditText et_update_user_firstname;
    private EditText et_update_user_name;
    private EditText et_update_user_mail;
    private EditText et_update_user_pwd;
    private EditText et_update_user_pwdconf;
    private Button bt_update_user_update;
    private Toolbar tb_update_user_toolbar;

    /**
     * DB references.
     */
    private UserAccessDB userDB;
    private Bundle extra;
    private int data;

    /**
     * Globals variables.
     */
    private boolean firstnameOk;
    private boolean nameOk;
    private boolean mailOk;
    private boolean pwdOk;

    /**
     * Method called at the creation of the activity.
     *
     * @param savedInstanceState save the state of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        et_update_user_firstname = findViewById(R.id.et_update_user_firstname);
        et_update_user_name = findViewById(R.id.et_update_user_name);
        et_update_user_mail = findViewById(R.id.et_update_user_mail);
        et_update_user_pwd = findViewById(R.id.et_update_user_pwd);
        et_update_user_pwdconf = findViewById(R.id.et_update_user_pwdconf);
        bt_update_user_update = findViewById(R.id.bt_update_user_update);
        tb_update_user_toolbar = findViewById(R.id.tb_update_user_toolbar);

        setActionBar(tb_update_user_toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        extra = this.getIntent().getExtras();
        if(extra != null) {
            data = Integer.parseInt(extra.getString("id"));
        } else {
            Toast.makeText(getApplicationContext(), "Error: data not found", Toast.LENGTH_SHORT).show();
            startActivity( new Intent(this,LoginActivity.class));
        }

        userDB = new UserAccessDB(this);
        userDB.openForRead();
        User user = userDB.getUserById(data);
        userDB.close();

        et_update_user_firstname.setText(user.getFirstName());
        et_update_user_name.setText(user.getName());
        et_update_user_mail.setText(user.getMail());
    }

    /**
     * Manages the different buttons of the activity.
     *
     * @param v view of the activity
     */
    public void OnModifyUserClickManager(View v) {
        switch (v.getId()) {
            case R.id.bt_update_user_update:
                if(!et_update_user_firstname.getText().toString().isEmpty()
                        && !et_update_user_name.getText().toString().isEmpty()
                        && !et_update_user_mail.getText().toString().isEmpty()
                        && !et_update_user_pwd.getText().toString().isEmpty()
                        && !et_update_user_pwdconf.getText().toString().isEmpty()
                        && et_update_user_pwd.getText().toString().equals(et_update_user_pwdconf.getText().toString())) {

                    firstnameOk = false;
                    nameOk = false;
                    mailOk = false;
                    pwdOk = false;

                    if (et_update_user_firstname.getText().toString().matches("[a-zA-Zàéèêëïùç]+")) {
                        firstnameOk = true;
                    }
                    if (et_update_user_name.getText().toString().matches("[a-zA-Zàéèêëïùç]+")) {
                        nameOk = true;
                    }
                    if (et_update_user_mail.getText().toString().matches("[a-zA-Z_0-9]+@([a-zA-Z]+.)+[a-zA-Z]{2,4}")) {
                        mailOk = true;
                    }
                    if (et_update_user_pwd.getText().toString().matches("[a-zA-Z_0-9]{4,}")) {
                        pwdOk = true;
                    }
                    if(firstnameOk && nameOk && mailOk && pwdOk) {
                        try {
                            String hashedPwd = hashPassword(et_update_user_pwd.getText().toString());

                            User user = new User(et_update_user_firstname.getText().toString(),
                                    et_update_user_name.getText().toString(),
                                    et_update_user_mail.getText().toString(),
                                    hashedPwd);

                            userDB.openForWrite();
                            userDB.updateUser(data, user);
                            userDB.close();
                            Toast.makeText(getApplicationContext(),"User updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this,UsersActivity.class));
                        } catch (NoSuchAlgorithmException e) {
                            Toast.makeText(this, "Error: password can't be hashed!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),"Error: invalid syntax",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Error: uncompleted fields or non-matching passwords",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }
}
