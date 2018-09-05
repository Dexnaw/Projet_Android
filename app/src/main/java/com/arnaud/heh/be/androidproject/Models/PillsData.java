package com.arnaud.heh.be.androidproject.Models;

/**
 * Model of a group of boolean used to save data in pills automaton.
 *
 * @author Arnaud Urbain
 */

public class PillsData {

    /**
     * Parameters.
     */
    private boolean on;
    private boolean detectFilling;
    private boolean detectCorking;
    private boolean detectPills;
    private boolean genBottle;
    private boolean onlineAccess;
    private boolean distribPillContact;
    private boolean engineBandContact;
    private boolean demand5;
    private boolean demand10;
    private boolean demand15;
    private int pillsNumber;
    private int bottlesNumber;


    /**
     * Constructor.
     */
    public PillsData(){
        this.on = false;
        this.detectFilling = false;
        this.detectCorking = false;
        this.detectPills = false;
        this.genBottle = false;
        this.onlineAccess = false;
        this.distribPillContact = false;
        this.engineBandContact = false;
        this.demand5 = false;
        this.demand10 = false;
        this.demand15 = false;
        this.pillsNumber = 0;
        this.bottlesNumber = 0;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isDetectFilling() {
        return detectFilling;
    }

    public void setDetectFilling(boolean detectFilling) {
        this.detectFilling = detectFilling;
    }

    public boolean isDetectCorking() {
        return detectCorking;
    }

    public void setDetectCorking(boolean detectCorking) {
        this.detectCorking = detectCorking;
    }

    public boolean isEngineBandContact() {
        return engineBandContact;
    }

    public void setEngineBandContact(boolean engineBandContact) {
        this.engineBandContact = engineBandContact;
    }

    public boolean isDemand5() {
        return demand5;
    }

    public void setDemand5(boolean demand5) {
        this.demand5 = demand5;
    }

    public boolean isDemand10() {
        return demand10;
    }

    public void setDemand10(boolean demand10) {
        this.demand10 = demand10;
    }

    public boolean isDemand15() {
        return demand15;
    }

    public void setDemand15(boolean demand15) {
        this.demand15 = demand15;
    }

    public int getPillsNumber() {
        return pillsNumber;
    }

    public void setPillsNumber(int pillsNumber) {
        this.pillsNumber = pillsNumber;
    }

    public int getBottlesNumber() {
        return bottlesNumber;
    }

    public void setBottlesNumber(int bottlesNumber) {
        this.bottlesNumber = bottlesNumber;
    }

    public boolean isDetectPills() {
        return detectPills;
    }

    public void setDetectPills(boolean detectPills) {
        this.detectPills = detectPills;
    }

    public boolean isGenBottle() {
        return genBottle;
    }

    public void setGenBottle(boolean genBottle) {
        this.genBottle = genBottle;
    }

    public boolean isOnlineAccess() {
        return onlineAccess;
    }

    public void setOnlineAccess(boolean onlineAccess) {
        this.onlineAccess = onlineAccess;
    }

    public boolean isDistribPillContact() {
        return distribPillContact;
    }

    public void setDistribPillContact(boolean distribPillContact) {
        this.distribPillContact = distribPillContact;
    }
}
