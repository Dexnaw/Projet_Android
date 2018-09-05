package com.arnaud.heh.be.androidproject.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.arnaud.heh.be.androidproject.Database.AutomatonAccessDB;
import com.arnaud.heh.be.androidproject.Models.Automaton;
import com.arnaud.heh.be.androidproject.R;

import java.util.ArrayList;

/**
 * Register of automatons screen.
 *
 * @author Arnaud Urbain
 */
public class RegisterAutomatonActivity extends Activity {

    /**
     * UI references.
     */
    private EditText et_registerautomaton_name;
    private Spinner sp_registerautomaton_type;
    private EditText et_registerautomaton_racknumber;
    private EditText et_registerautomaton_slotnumber;
    private Button bt_registerautomaton_add;
    private Toolbar tb_registerautomaton_toolbar;

    /**
     * DB references.
     */
    private AutomatonAccessDB automatonDB;
    private ArrayList<Automaton> tabautomaton;

    /**
     * Globals variables.
     */
    private boolean nameOk;
    private boolean rackOk;
    private boolean slotOk;
    private boolean checknameOk;

    /**
     * Method called at the creation of the activity.
     *
     * @param savedInstanceState save the state of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_automaton);

        et_registerautomaton_name = findViewById(R.id.et_registerautomaton_name);
        sp_registerautomaton_type = findViewById(R.id.sp_registerautomaton_type);
        et_registerautomaton_racknumber = findViewById(R.id.et_registerautomaton_racknumber);
        et_registerautomaton_slotnumber = findViewById(R.id.et_registerautomaton_slotnumber);
        bt_registerautomaton_add = findViewById(R.id.bt_registerautomaton_add);
        tb_registerautomaton_toolbar = findViewById(R.id.tb_registerautomaton_toolbar);

        setActionBar(tb_registerautomaton_toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Manages the different buttons of the activity.
     *
     * @param v view of the activity
     */
    public void OnRegisterAutomatonClickManager(View v) {
        switch (v.getId()) {
            case R.id.bt_registerautomaton_add:
                if(!et_registerautomaton_name.getText().toString().isEmpty()
                        && !et_registerautomaton_racknumber.getText().toString().isEmpty()
                        && !et_registerautomaton_slotnumber.getText().toString().isEmpty()){

                    nameOk = false;
                    rackOk = false;
                    slotOk = false;

                    if(et_registerautomaton_name.getText().toString().matches("[a-zA-Z0-9àéèêëïùç]+")){
                        nameOk = true;
                    }
                    if(et_registerautomaton_racknumber.getText().toString().matches("[0-9]+")){
                        rackOk = true;
                    }
                    if(et_registerautomaton_slotnumber.getText().toString().matches("[0-9]+")) {
                        slotOk = true;
                    }
                    if(nameOk && rackOk && slotOk) {
                        Automaton newAutomaton = new Automaton(et_registerautomaton_name.getText().toString(),
                                sp_registerautomaton_type.getSelectedItem().toString(),
                                Integer.parseInt(et_registerautomaton_racknumber.getText().toString()),
                                Integer.parseInt(et_registerautomaton_slotnumber.getText().toString()));
                        automatonDB = new AutomatonAccessDB(this);
                        automatonDB.openForRead();
                        tabautomaton = automatonDB.getAllAutomaton();
                        automatonDB.close();

                        checknameOk= true;
                        for(int i = 0; i < tabautomaton.size(); i++){
                            Automaton automaton = tabautomaton.get(i);
                            if(automaton.getName().equals(et_registerautomaton_name.getText().toString())){
                                checknameOk = false;
                            }
                        }
                        if(checknameOk){
                            automatonDB.openForWrite();
                            automatonDB.insertAutomaton(newAutomaton);
                            automatonDB.close();
                            Toast.makeText(getApplicationContext(), "New automaton added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AutomatonsActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(),"Error: name already used",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"Error: invalid syntax",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Error: uncompleted fields",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
