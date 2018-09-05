package com.arnaud.heh.be.androidproject.Models;

import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arnaud.heh.be.androidproject.Simatic_S7.S7;
import com.arnaud.heh.be.androidproject.Simatic_S7.S7Client;
import com.arnaud.heh.be.androidproject.Simatic_S7.S7OrderCode;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Model for the reader of the pill automaton.
 *
 * @author Arnaud Urbain
 */

public class ReadPillS7 {

    /**
     * Globals variables.
     */
    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private PillsData td;

    /**
     * U.I. references.
     */
    private EditText et_pill_ip;
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
    private View vi_pill_ui;

    /**
     * Network references.
     */
    private AutomateS7 plcS7;
    private Thread readThread;
    private S7Client comS7;
    private String[] param = new String[10];
    private byte[] datasPLC = new byte[512];

    /**
     * Constructor of the reader.
     *
     * @param v view of the activity
     */
    public ReadPillS7(View v, EditText e, CheckBox c1, CheckBox c2, CheckBox c3, CheckBox c4, CheckBox c5,
                      CheckBox c6, CheckBox c7, CheckBox c8, CheckBox c9, CheckBox c10, CheckBox c11,
                      TextView t1, TextView t2){
        vi_pill_ui = v;
        et_pill_ip = e;
        cb_pill_on = c1;
        cb_pill_detectfilling = c2;
        cb_pill_detectbouchonning = c3;
        cb_pill_detectpills = c4;
        cb_pill_genbottles = c5;
        cb_pill_online = c6;
        cb_pill_distribpillcontact = c7;
        cb_pill_enginebandcontact = c8;
        cb_pill_demand5 = c9;
        cb_pill_demand10 = c10;
        cb_pill_demand15 = c11;
        tv_pill_numpills = t1;
        tv_pill_numbottles = t2;

        comS7 = new S7Client();
        plcS7 = new AutomateS7();

        readThread = new Thread(plcS7);

        td = new PillsData();
    }

    /**
     * Start method.
     *
     * @param a IP Address
     * @param r RackNumber
     * @param s SlotNumber
     */
    public void start(String a, String r, String s){
        if(!readThread.isAlive()){
            param[0] = a;
            param[1] = r;
            param[2] = s;

            readThread.start();
            isRunning.set(true);
        }
    }

    /**
     * Stop method.
     */
    public  void stop(){
        isRunning.set(false);
        comS7.Disconnect();
        readThread.interrupt();
    }

    /**
     * Method used when the thread begin.
     *
     * @param t API reference
     */
    private void downloadOnPreExecute(int t){
        Toast.makeText(vi_pill_ui.getContext(), "Thread started!", Toast.LENGTH_SHORT).show();
        et_pill_ip.setText("PLC : " + String.valueOf(t));
        et_pill_ip.setEnabled(false);
    }

    /**
     * Method used during the thread.
     */
    private void downloadOnProgressUpdate() {
        cb_pill_on.setChecked(td.isOn());
        cb_pill_detectfilling.setChecked(td.isDetectFilling());
        cb_pill_detectbouchonning.setChecked(td.isDetectCorking());
        cb_pill_detectpills.setChecked(td.isDetectPills());
        cb_pill_genbottles.setChecked(td.isGenBottle());
        cb_pill_online.setChecked(td.isOnlineAccess());
        cb_pill_distribpillcontact.setChecked(td.isDistribPillContact());
        cb_pill_enginebandcontact.setChecked(td.isEngineBandContact());
        cb_pill_demand5.setChecked(td.isDemand5());
        cb_pill_demand10.setChecked(td.isDemand10());
        cb_pill_demand15.setChecked(td.isDemand15());
        tv_pill_numpills.setText("Pill Number = " + td.getPillsNumber());
        tv_pill_numbottles.setText("Bottle Number = " + td.getBottlesNumber());
    }

    /**
     * Method used when the thread finish.
     */
    private void downloadOnPostExecute(){
        Toast.makeText(vi_pill_ui.getContext(), "Thread ended!", Toast.LENGTH_SHORT).show();
        et_pill_ip.setText("");
        et_pill_ip.setEnabled(true);
    }

    /**
     * Handler of reader class
     */
    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute(msg.arg1);
                    break;
                case MESSAGE_PROGRESS_UPDATE:
                    downloadOnProgressUpdate();
                    break;
                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Thread used when activated.
     */
    private class AutomateS7 implements Runnable{
        @Override
        public void run(){
            try{
                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res = comS7.ConnectTo(param[0],Integer.valueOf(param[1]), Integer.valueOf(param[2]));
                S7OrderCode orderCode = new S7OrderCode();
                Integer result = comS7.GetOrderCode(orderCode);
                int numCPU = -1;
                if(res.equals(0) && result.equals(0)){
                    numCPU = Integer.valueOf(orderCode.Code().toString().substring(5,8));
                } else {
                    numCPU = 0000;
                }
                sendPreExecuteMessage(numCPU);
                while(isRunning.get()){
                    if(res.equals(0)){
                        int retInfo = comS7.ReadArea(S7.S7AreaDB,5,0,20,datasPLC);
                        if(retInfo == 0){
                            sendProgressMessage();
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                sendPostExecuteMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void sendPreExecuteMessage(int v){
            Message preExecuteMsg = new Message();
            preExecuteMsg.what = MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1 = v;
            handler.sendMessage(preExecuteMsg);
        }

        private void sendPostExecuteMessage(){
            Message postExecuteMsg = new Message();
            postExecuteMsg.what = MESSAGE_POST_EXECUTE;
            handler.sendMessage(postExecuteMsg);
        }

        private void sendProgressMessage(){
            setPillsData();
            Message progressMsg = new Message();
            progressMsg.what = MESSAGE_PROGRESS_UPDATE;
            handler.sendMessage(progressMsg);
        }

        private void setPillsData(){
            td.setOn(S7.GetBitAt(datasPLC, 0, 0));
            td.setDetectFilling(S7.GetBitAt(datasPLC,0, 4));
            td.setDetectCorking(S7.GetBitAt(datasPLC, 0, 5));
            td.setDetectPills(S7.GetBitAt(datasPLC,0,6));
            td.setGenBottle(S7.GetBitAt(datasPLC, 1, 3));
            td.setOnlineAccess(S7.GetBitAt(datasPLC,1,6));
            td.setDistribPillContact(S7.GetBitAt(datasPLC,4,0));
            td.setEngineBandContact(S7.GetBitAt(datasPLC,4,1));
            td.setDemand5(S7.GetBitAt(datasPLC,4,3));
            td.setDemand10(S7.GetBitAt(datasPLC,4,4));
            td.setDemand15(S7.GetBitAt(datasPLC,4,5));
            td.setPillsNumber(S7.BCDtoByte(datasPLC[15]));
            td.setBottlesNumber(S7.GetWordAt(datasPLC, 16));
        }

    }
}
