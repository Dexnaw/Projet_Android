package com.arnaud.heh.be.androidproject.Models;

import android.util.Log;

import com.arnaud.heh.be.androidproject.Simatic_S7.S7;
import com.arnaud.heh.be.androidproject.Simatic_S7.S7Client;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Model for the writer of the automaton.
 *
 * @author Arnaud Urbain
 */

public class WriteTaskS7 {

    /**
     * Globals variables.
     */
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * Network references.
     */
    private Thread writeThread;
    private AutomateS7 plcS7;
    private S7Client comS7;
    private String[] param = new String[10];
    private byte[] wordCommand = new byte[10];

    /**
     * Constructor of the writer.
     */
    public WriteTaskS7(){

    }

    /**
     * Stop method.
     */
    public void stop(){
        isRunning.set(false);
        comS7.Disconnect();
        writeThread.interrupt();
    }

    /**
     * Start method.
     *
     * @param a IP Address
     * @param r RackNumber
     * @param s SlotNumber
     */
    public void start(String a, String r, String s){
        if(!writeThread.isAlive()){
            param[0] = a;
            param[1] = r;
            param[2] = s;

            writeThread.start();
            isRunning.set(true);
        }
    }

    /**
     * Thread used when activated.
      */
    private class AutomateS7 implements Runnable {
        @Override
        public void run(){
            try{
                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res = comS7.ConnectTo(param[0], Integer.valueOf(param[1]), Integer.valueOf(param[2]));
                while(isRunning.get() && (res.equals(0))){
                    Integer writePLC = comS7.WriteArea(S7.S7AreaDB,34, 0, 1, wordCommand);
                    if(writePLC.equals(0)){
                        Log.i("ret WRITE : ", String.valueOf(res) + "*****" + String.valueOf(writePLC));
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setWriteBool(int b, int v){
        if(v==1){
            wordCommand[0] = (byte) (b | wordCommand[0]);
        } else {
            wordCommand[0] = (byte) (~b & wordCommand[0]);
        }
    }

}
