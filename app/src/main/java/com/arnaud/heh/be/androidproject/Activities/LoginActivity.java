package com.arnaud.heh.be.androidproject.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arnaud.heh.be.androidproject.Database.UserAccessDB;
import com.arnaud.heh.be.androidproject.Models.User;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.arnaud.heh.be.androidproject.Models.HashPassword.hashPassword;
import com.arnaud.heh.be.androidproject.R;

/**
 * Login screen.
 *
 * @author Arnaud Urbain
 */
public class LoginActivity extends Activity {

    /**
     * UI references.
     */
    private EditText et_login_mail;
    private EditText et_login_pwd;
    private Button bt_login_ok;
    private Button bt_login_register;

    /**
     * DB references.
     */
    private UserAccessDB userDB;
    private ArrayList<User> tabuser;

    /**
     * Globals variables.
     */
    private boolean isOk;

    /**
     * Method called at the creation of the activity.
     *
     * @param savedInstanceState save the state of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_login_mail = findViewById(R.id.et_login_mail);
        et_login_pwd = findViewById(R.id.et_login_pwd);
        bt_login_ok = findViewById(R.id.bt_login_ok);
        bt_login_register = findViewById(R.id.bt_login_register);
    }

    /**
     * Manages the different buttons of the activity.
     *
     * @param v view of the activity
     */
    public void onLoginClickManager(View v) {
        switch (v.getId()) {

            case R.id.bt_login_ok:
                if (!et_login_mail.getText().toString().isEmpty()
                        && !et_login_pwd.getText().toString().isEmpty()) {
                    userDB = new UserAccessDB(this);
                    userDB.openForRead();
                    tabuser = userDB.getAllUser();
                    userDB.close();

                    isOk = false;
                    for (int i = 0; i < tabuser.size(); i++) {
                        User user = tabuser.get(i);
                        try {
                            String hashedPwd = hashPassword(et_login_pwd.getText().toString());
                            if (user.getMail().equals(et_login_mail.getText().toString()) && user.getPwd().equals(hashedPwd)) {
                                isOk = true;
                                if (user.getRights().toString().equals("admin")) {
                                    startActivity(new Intent(getApplicationContext(), UsersActivity.class));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), AutomatonsActivity.class));
                                }
                            }
                        } catch(NoSuchAlgorithmException e) {
                            Toast.makeText(this, "Error: password can't be hashed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(!isOk) {
                        Toast.makeText(getApplicationContext(), "Error: User not registered",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Uncompleted fields",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_login_register:
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                break;

            default:
                break;
        }
    }
}
