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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


import com.arnaud.heh.be.androidproject.R;
import static com.arnaud.heh.be.androidproject.Models.HashPassword.hashPassword;

/**
 * Register screen.
 *
 * @author Arnaud Urbain
 */
public class RegisterActivity extends Activity {

    /**
     * UI references.
     */
    private EditText et_register_firstname;
    private EditText et_register_name;
    private EditText et_register_mail;
    private EditText et_register_pwd;
    private EditText et_register_pwdconf;
    private Button bt_register_signup;
    private Toolbar tb_register_toolbar;

    /**
     * DB references.
     */
    private UserAccessDB userDB;
    private ArrayList<User> tabuser;

    /**
     * Globals variables.
     */
    private boolean firstnameOk;
    private boolean nameOk;
    private boolean mailOk;
    private boolean pwdOk;
    private boolean checkmailOk;

    /**
     * Method called at the creation of the activity.
     *
     * @param savedInstanceState save the state of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        et_register_firstname = findViewById(R.id.et_register_firstname);
        et_register_name = findViewById(R.id.et_register_name);
        et_register_mail = findViewById(R.id.et_register_mail);
        et_register_pwd = findViewById(R.id.et_register_pwd);
        et_register_pwdconf = findViewById(R.id.et_register_pwdconf);
        bt_register_signup = findViewById(R.id.bt_register_signup);

    }

    /**
     * Manages the different buttons of the activity.
     *
     * @param v view of the activity
     */
    public void OnRegistrationClickManager(View v) {
        switch (v.getId()) {
            case R.id.bt_register_signup:
                if(!et_register_firstname.getText().toString().isEmpty()
                        && !et_register_name.getText().toString().isEmpty()
                        && !et_register_mail.getText().toString().isEmpty()
                        && !et_register_pwd.getText().toString().isEmpty()
                        && !et_register_pwdconf.getText().toString().isEmpty()
                        && et_register_pwd.getText().toString().equals(et_register_pwdconf.getText().toString())) {

                    firstnameOk = false;
                    nameOk = false;
                    mailOk = false;
                    pwdOk = false;

                    if(et_register_firstname.getText().toString().matches("[a-zA-Zàéèêëïùç]+")){
                        firstnameOk = true;
                    }
                    if(et_register_name.getText().toString().matches("[a-zA-Zàéèêëïùç]+")){
                        nameOk = true;
                    }
                    if(et_register_mail.getText().toString().matches("[a-zA-Z_0-9]+@([a-zA-Z]+.)+[a-zA-Z]{2,4}")){
                        mailOk = true;
                    }
                    if(et_register_pwd.getText().toString().matches("[a-zA-Z_0-9]{4,}")){
                        pwdOk = true;
                    }
                    if(firstnameOk && nameOk && mailOk && pwdOk){
                        String pwdHashed;
                        try {
                            pwdHashed = hashPassword(et_register_pwd.getText().toString());
                            User newUser = new User(et_register_firstname.getText().toString(),
                                    et_register_name.getText().toString(),
                                    et_register_mail.getText().toString(),
                                    pwdHashed);

                            userDB = new UserAccessDB(this);
                            userDB.openForRead();
                            tabuser = userDB.getAllUser();
                            userDB.close();
                            if(tabuser.isEmpty()){
                                newUser.setRights("admin");
                                userDB.openForWrite();
                                userDB.insertUser(newUser);
                                userDB.close();
                                Toast.makeText(getApplicationContext(),"New admin added",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            } else {
                                checkmailOk = true;
                                for(int i = 0; i < tabuser.size(); i++){
                                    User user = tabuser.get(i);
                                    if(user.getMail().equals(et_register_mail.getText().toString())){
                                        checkmailOk = false;
                                    }
                                }
                                if(checkmailOk){
                                    userDB.openForWrite();
                                    userDB.insertUser(newUser);
                                    userDB.close();
                                    Toast.makeText(getApplicationContext(),"New user added",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(),"Error: mail address already used",Toast.LENGTH_SHORT).show();
                                }
                            }
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
