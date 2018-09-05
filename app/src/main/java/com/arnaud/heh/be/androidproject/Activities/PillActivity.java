package com.arnaud.heh.be.androidproject.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.arnaud.heh.be.androidproject.Database.AutomatonAccessDB;
import com.arnaud.heh.be.androidproject.Models.Automaton;
import com.arnaud.heh.be.androidproject.Models.ReadPillS7;

import com.arnaud.heh.be.androidproject.R;

/**
 * Connection screen to a pill automaton.
 *
 * @author Arnaud Urbain
 */
public class PillActivity extends Activity {

    /**
     * UI references.
     */
    private TextView tv_pill_title;
    private EditText et_pill_ip;
    private Button bt_pill_connect;
    private Toolbar tb_pill_toolbar;
    private CheckBox cb_pill_on;
    private CheckBox cb_pill_detectfilling;
    private CheckBox cb_pill_detectbouchonning;
    private CheckBox cb_pill_detectpills;
    private CheckBox cb_pill_genbottles;
    private CheckBox cb_pill_online;
    private CheckBox cb_pill_distribpillcontact;
    private CheckBox cb_pill_enginebandcontact;
    private CheckBox cb_pill_demand5;
    private CheckBox cb_pill_demand10;
    private CheckBox cb_pill_demand15;
    private TextView tv_pill_numpills;
    private TextView tv_pill_numbottles;

    /**
     * Internet references.
     */
    private ReadPillS7 readS7;
    private NetworkInfo network;
    private ConnectivityManager connexStatus;

    /**
     * DB references.
     */
    private AutomatonAccessDB automatonDB;
    private Bundle extra;
    private int data;
    private Automaton automaton;

    /**
     * Method called at the creation of the activity.
     *
     * @param savedInstanceState save the state of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pills);

        tv_pill_title = findViewById(R.id.tv_pill_title);
        et_pill_ip = findViewById(R.id.et_pill_ip);
        bt_pill_connect = findViewById(R.id.bt_pill_connect);
        tb_pill_toolbar = findViewById(R.id.tb_pill_toolbar);
        cb_pill_on = findViewById(R.id.cb_pill_on);
        cb_pill_detectfilling = findViewById(R.id.cb_pill_detectfilling);
        cb_pill_detectbouchonning = findViewById(R.id.cb_pill_detectbouchonning);
        cb_pill_detectpills = findViewById(R.id.cb_pill_detectpills);
        cb_pill_genbottles = findViewById(R.id.cb_pill_genbottles);
        cb_pill_online = findViewById(R.id.cb_pill_online);
        cb_pill_distribpillcontact = findViewById(R.id.cb_pill_distribpillcontact);
        cb_pill_enginebandcontact = findViewById(R.id.cb_pill_enginebandcontact);
        cb_pill_demand5 = findViewById(R.id.cb_pill_demand5);
        cb_pill_demand10 = findViewById(R.id.cb_pill_demand10);
        cb_pill_demand15 = findViewById(R.id.cb_pill_demand15);
        tv_pill_numpills = findViewById(R.id.tv_pill_numpills);
        tv_pill_numbottles = findViewById(R.id.tv_pill_numbottles);

        setActionBar(tb_pill_toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        extra = this.getIntent().getExtras();
        if(extra != null) {
            data = Integer.parseInt(extra.getString("id"));
        } else {
            Toast.makeText(getApplicationContext(), "Error: data not found", Toast.LENGTH_SHORT).show();
            startActivity( new Intent(this,AutomatonsActivity.class));
        }

        automatonDB = new AutomatonAccessDB(this);
        automatonDB.openForRead();
        automaton = automatonDB.getAutomatonById(data);
        automatonDB.close();

        connexStatus = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = connexStatus.getActiveNetworkInfo();
    }

    /**
     * Manages the different buttons of the activity.
     *
     * @param v view of the activity
     */
    public void OnPillClickManager(View v) {
        switch (v.getId()) {
            case R.id.bt_pill_connect:
                if(et_pill_ip.getText().toString().matches("([0-9]{1,3}\\.){3}[0-9]{1,3}")) {
                    if(network != null && network.isConnectedOrConnecting()){
                        if(bt_pill_connect.getText().equals("CONNECT")) {
                            Toast.makeText(this, network.getTypeName(), Toast.LENGTH_SHORT).show();
                            bt_pill_connect.setText("DISCONNECT");
                            tv_pill_title.setText("Pill packaging (S7‐1516 2DPPN)");
                            readS7 = new ReadPillS7(v, et_pill_ip, cb_pill_on, cb_pill_detectfilling, cb_pill_detectbouchonning,
                                    cb_pill_detectpills, cb_pill_genbottles, cb_pill_online, cb_pill_distribpillcontact, cb_pill_enginebandcontact,
                                    cb_pill_demand5, cb_pill_demand10, cb_pill_demand15, tv_pill_numpills, tv_pill_numbottles);
                            readS7.start(et_pill_ip.getText().toString(), String.valueOf(automaton.getRackNumber()), String.valueOf(automaton.getSlotNumber()));
                        } else {
                            readS7.stop();
                            bt_pill_connect.setText("CONNECT");
                            tv_pill_title.setText("Complete IP address:");
                            Toast.makeText(this, "Connection to the automaton interrupted by the user.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "Error: impossible connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error: invalid ip address", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
