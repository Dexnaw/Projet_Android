package com.arnaud.heh.be.androidproject.Models;

/**
 * Model Automaton.
 *
 * @author Arnaud Urbain
 */

public class Automaton {

    /**
     * Parameters.
     */
    private int id;
    private String name;
    private String type;
    private int rackNumber;
    private int slotNumber;

    /**
     * Default constructor.
     */
    public Automaton(){}

    /**
     * Constructor.
     *
     * @param name name of the automaton
     * @param type type of automaton
     */
    public Automaton(String name, String type, int rackNumber, int slotNumber){
        this.name = name;
        this.type = type;
        this.rackNumber = rackNumber;
        this.slotNumber = slotNumber;
    }

    /**
     * Create a string with the automaton's parameters.
     *
     * @return a string corresponding to the automaton
     */

    @Override
    public String toString() {
        return "Automaton{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", rackNumber=" + rackNumber +
                ", slotNumber=" + slotNumber +
                '}';
    }

    /**
     * Gets the id of the automaton.
     *
     * @return id of the automaton
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the automaton.
     *
     * @param id id of the automaton
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the automaton.
     *
     * @return name of the automaton
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the automaton.
     *
     * @param name name of the automaton
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type of the automaton.
     *
     * @return type of the automaton
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the automaton.
     *
     * @param type type of the automaton
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the rack number of the automaton.
     *
     * @return rack number of the automaton
     */
    public int getRackNumber() {
        return rackNumber;
    }

    /**
     * Sets the rack number of the automaton.
     *
     * @param rackNumber rack number of the automaton
     */
    public void setRackNumber(int rackNumber) {
        this.rackNumber = rackNumber;
    }

    /**
     * Gets the slot number of the automaton.
     *
     * @return slot number of the automaton
     */
    public int getSlotNumber() {
        return slotNumber;
    }

    /**
     * Sets the slot number of the automaton.
     *
     * @param slotNumber slot number of the automaton
     */
    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

}