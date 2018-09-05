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
import com.arnaud.heh.be.androidproject.Models.ReadFluidS7;
import com.arnaud.heh.be.androidproject.R;


/**
 * Connection screen to a fluid automaton.
 *
 * @author Arnaud Urbain
 */
public class FluidActivity extends Activity {

    /**
     * UI references.
     */
    private TextView tv_fluid_title;
    private EditText et_fluid_ip;
    private Button bt_fluid_connect;
    private Toolbar tb_fluid_toolbar;
    private CheckBox cb_fluid_sevalve1;
    private CheckBox cb_fluid_sevalve2;
    private CheckBox cb_fluid_sevalve3;
    private CheckBox cb_fluid_sevalve4;
    private CheckBox cb_fluid_manual;
    private CheckBox cb_fluid_online;
    private CheckBox cb_fluid_qvalve1;
    private CheckBox cb_fluid_qvalve2;
    private CheckBox cb_fluid_qvalve3;
    private CheckBox cb_fluid_qvalve4;
    private TextView tv_fluid_fluidlevel;
    private TextView tv_fluid_consignauto;
    private TextView tv_fluid_consignman;
    private TextView tv_fluid_valvecontrolword;

    /**
     * Internet references.
     */
    private ReadFluidS7 readS7;
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
        setContentView(R.layout.activity_fluid);

        tv_fluid_title = findViewById(R.id.tv_fluid_title);
        et_fluid_ip = findViewById(R.id.et_fluid_ip);
        bt_fluid_connect = findViewById(R.id.bt_fluid_connect);
        tb_fluid_toolbar = findViewById(R.id.tb_fluid_toolbar);
        cb_fluid_sevalve1 = findViewById(R.id.cb_fluid_sevalve1);
        cb_fluid_sevalve2 = findViewById(R.id.cb_fluid_sevalve2);
        cb_fluid_sevalve3 = findViewById(R.id.cb_fluid_sevalve3);
        cb_fluid_sevalve4 = findViewById(R.id.cb_fluid_sevalve4);
        cb_fluid_manual = findViewById(R.id.cb_fluid_manual);
        cb_fluid_online = findViewById(R.id.cb_fluid_online);
        cb_fluid_qvalve1 = findViewById(R.id.cb_fluid_qvalve1);
        cb_fluid_qvalve2 = findViewById(R.id.cb_fluid_qvalve2);
        cb_fluid_qvalve3 = findViewById(R.id.cb_fluid_qvalve3);
        cb_fluid_qvalve4 = findViewById(R.id.cb_fluid_qvalve4);
        tv_fluid_fluidlevel = findViewById(R.id.tv_fluid_fluidlevel);
        tv_fluid_consignauto = findViewById(R.id.tv_fluid_consignauto);
        tv_fluid_consignman = findViewById(R.id.tv_fluid_consignman);
        tv_fluid_valvecontrolword = findViewById(R.id.tv_fluid_valvecontrolword);

        setActionBar(tb_fluid_toolbar);
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
    public void OnFluidClickManager(View v) {
        switch (v.getId()) {
            case R.id.bt_fluid_connect:
                if(et_fluid_ip.getText().toString().matches("([0-9]{1,3}\\.){3}[0-9]{1,3}")) {
                    if (network != null && network.isConnectedOrConnecting()) {
                        if (bt_fluid_connect.getText().equals("CONNECT")) {
                            Toast.makeText(this, network.getTypeName(), Toast.LENGTH_SHORT).show();
                            bt_fluid_connect.setText("DISCONNECT");
                            tv_fluid_title.setText("Fluid level control (S7‐1214C)");
                            readS7 = new ReadFluidS7(v, et_fluid_ip, cb_fluid_sevalve1, cb_fluid_sevalve2, cb_fluid_sevalve3,
                                    cb_fluid_sevalve4, cb_fluid_manual, cb_fluid_online, cb_fluid_qvalve1, cb_fluid_qvalve2, cb_fluid_qvalve3, cb_fluid_qvalve4,
                                    tv_fluid_fluidlevel, tv_fluid_consignauto, tv_fluid_consignman, tv_fluid_valvecontrolword);
                            readS7.start(et_fluid_ip.getText().toString(), String.valueOf(automaton.getRackNumber()), String.valueOf(automaton.getSlotNumber()));
                        } else {
                            readS7.stop();
                            bt_fluid_connect.setText("CONNECT");
                            tv_fluid_title.setText("Complete IP address:");
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
