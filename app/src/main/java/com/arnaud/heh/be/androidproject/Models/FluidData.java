package com.arnaud.heh.be.androidproject.Models;

/**
 * Model of a group of boolean used to save data in fluid automaton.
 *
 * @author Arnaud Urbain
 */

public class FluidData {

    /**
     * Parameters.
     */
    private boolean se_valve1;
    private boolean se_valve2;
    private boolean se_valve3;
    private boolean se_valve4;
    private boolean se_manual;
    private boolean online_access;
    private boolean q_valve1;
    private boolean q_valve2;
    private boolean q_valve3;
    private boolean q_valve4;
    private int fluidlevel;
    private int consign_auto;
    private int consign_man;
    private int valvecontrolword;

    /**
     * Constructor.
     */
    public FluidData(){
        se_valve1 = false;
        se_valve2 = false;
        se_valve3 = false;
        se_valve4 = false;
        se_manual = false;
        online_access = false;
        q_valve1 = false;
        q_valve2 = false;
        q_valve3 = false;
        q_valve4 = false;
        fluidlevel = 0;
        consign_auto = 0;
        consign_man = 0;
        valvecontrolword = 0;
    }

    public boolean isSe_valve1() {
        return se_valve1;
    }

    public void setSe_valve1(boolean se_valve1) {
        this.se_valve1 = se_valve1;
    }

    public boolean isSe_valve2() {
        return se_valve2;
    }

    public void setSe_valve2(boolean se_valve2) {
        this.se_valve2 = se_valve2;
    }

    public boolean isSe_valve3() {
        return se_valve3;
    }

    public void setSe_valve3(boolean se_valve3) {
        this.se_valve3 = se_valve3;
    }

    public boolean isSe_valve4() {
        return se_valve4;
    }

    public void setSe_valve4(boolean se_valve4) {
        this.se_valve4 = se_valve4;
    }

    public boolean isSe_manual() {
        return se_manual;
    }

    public void setSe_manual(boolean se_manual) {
        this.se_manual = se_manual;
    }

    public boolean isOnline_access() {
        return online_access;
    }

    public void setOnline_access(boolean online_access) {
        this.online_access = online_access;
    }

    public boolean isQ_valve1() {
        return q_valve1;
    }

    public void setQ_valve1(boolean q_valve1) {
        this.q_valve1 = q_valve1;
    }

    public boolean isQ_valve2() {
        return q_valve2;
    }

    public void setQ_valve2(boolean q_valve2) {
        this.q_valve2 = q_valve2;
    }

    public boolean isQ_valve3() {
        return q_valve3;
    }

    public void setQ_valve3(boolean q_valve3) {
        this.q_valve3 = q_valve3;
    }

    public boolean isQ_valve4() {
        return q_valve4;
    }

    public void setQ_valve4(boolean q_valve4) {
        this.q_valve4 = q_valve4;
    }

    public int getFluidlevel() {
        return fluidlevel;
    }

    public void setFluidlevel(int fluidlevel) {
        this.fluidlevel = fluidlevel;
    }

    public int getValvecontrolword() {
        return valvecontrolword;
    }

    public void setValvecontrolword(int valvecontrolword) {
        this.valvecontrolword = valvecontrolword;
    }
    public int getConsign_auto() {
        return consign_auto;
    }

    public void setConsign_auto(int consign_auto) {
        this.consign_auto = consign_auto;
    }

    public int getConsign_man() {
        return consign_man;
    }

    public void setConsign_man(int consign_man) {
        this.consign_man = consign_man;
    }
}
