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
 * Model for the reader of the fluid automaton.
 *
 * @author Arnaud Urbain
 */

public class ReadFluidS7 {

    /**
     * Globals variables.
     */
    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private FluidData fd;

    /**
     * U.I. references.
     */
    private EditText et_fluid_ip;
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
    private View vi_fluid_ui;

    /**
     * Network references.
     */
    private ReadFluidS7.AutomateS7 plcS7;
    private Thread readThread;
    private S7Client comS7;
    private String[] param = new String[10];
    private byte[] datasPLC = new byte[512];

    /**
     * Constructor of the reader.
     *
     * @param v view of the activity
     */
    public ReadFluidS7(View v, EditText e, CheckBox c1, CheckBox c2, CheckBox c3, CheckBox c4, CheckBox c5,
                       CheckBox c6, CheckBox c7, CheckBox c8, CheckBox c9, CheckBox c10, TextView t1,
                       TextView t2, TextView t3, TextView t4){
        vi_fluid_ui = v;
        et_fluid_ip = e;
        cb_fluid_sevalve1 = c1;
        cb_fluid_sevalve2 = c2;
        cb_fluid_sevalve3 = c3;
        cb_fluid_sevalve4 = c4;
        cb_fluid_manual = c5;
        cb_fluid_online = c6;
        cb_fluid_qvalve1 = c7;
        cb_fluid_qvalve2 = c8;
        cb_fluid_qvalve3 = c9;
        cb_fluid_qvalve4 = c10;
        tv_fluid_fluidlevel = t1;
        tv_fluid_consignauto = t2;
        tv_fluid_consignman = t3;
        tv_fluid_valvecontrolword = t4;


        comS7 = new S7Client();
        plcS7 = new AutomateS7();

        readThread = new Thread(plcS7);

        fd = new FluidData();
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
        Toast.makeText(vi_fluid_ui.getContext(), "Thread started!", Toast.LENGTH_SHORT).show();
        et_fluid_ip.setText("PLC : " + String.valueOf(t));
        et_fluid_ip.setEnabled(false);
    }

    /**
     * Method used during the thread.
     */
    private void downloadOnProgressUpdate() {
        cb_fluid_sevalve1.setChecked(fd.isSe_valve1());
        cb_fluid_sevalve2.setChecked(fd.isSe_valve2());
        cb_fluid_sevalve3.setChecked(fd.isSe_valve3());
        cb_fluid_sevalve4.setChecked(fd.isSe_valve4());
        cb_fluid_manual.setChecked(fd.isSe_manual());
        cb_fluid_online.setChecked(fd.isOnline_access());
        cb_fluid_qvalve1.setChecked(fd.isQ_valve1());
        cb_fluid_qvalve2.setChecked(fd.isQ_valve2());
        cb_fluid_qvalve3.setChecked(fd.isQ_valve3());
        cb_fluid_qvalve4.setChecked(fd.isQ_valve4());
        tv_fluid_fluidlevel.setText("Fluid Level = " + fd.getFluidlevel());
        tv_fluid_consignauto.setText("Consign Auto = " + fd.getConsign_auto());
        tv_fluid_consignman.setText("Consign Man = " + fd.getConsign_man());
        tv_fluid_valvecontrolword.setText("Valve Control Word = " + fd.getValvecontrolword());
    }

    /**
     * Method used when the thread finish.
     */
    private void downloadOnPostExecute(){
        Toast.makeText(vi_fluid_ui.getContext(), "Thread ended!", Toast.LENGTH_SHORT).show();
        et_fluid_ip.setText("");
        et_fluid_ip.setEnabled(true);
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
            setFluidData();
            Message progressMsg = new Message();
            progressMsg.what = MESSAGE_PROGRESS_UPDATE;
            handler.sendMessage(progressMsg);
        }

        private void setFluidData(){
            fd.setSe_valve1(S7.GetBitAt(datasPLC, 0, 1));
            fd.setSe_valve2(S7.GetBitAt(datasPLC, 0, 2));
            fd.setSe_valve3(S7.GetBitAt(datasPLC, 0, 3));
            fd.setSe_valve4(S7.GetBitAt(datasPLC, 0, 4));
            fd.setSe_manual(S7.GetBitAt(datasPLC, 0, 5));
            fd.setOnline_access(S7.GetBitAt(datasPLC, 0, 6));
            fd.setQ_valve1(S7.GetBitAt(datasPLC, 1, 1));
            fd.setQ_valve2(S7.GetBitAt(datasPLC, 1, 2));
            fd.setQ_valve3(S7.GetBitAt(datasPLC, 1, 3));
            fd.setQ_valve4(S7.GetBitAt(datasPLC, 1, 4));
            fd.setFluidlevel(S7.GetWordAt(datasPLC, 16));
            fd.setConsign_auto(S7.GetWordAt(datasPLC, 18));
            fd.setConsign_man(S7.GetWordAt(datasPLC, 20));
            fd.setValvecontrolword(S7.GetWordAt(datasPLC, 22));
        }

    }

}
