package com.arnaud.heh.be.androidproject.Models;

/**
 * Model User.
 *
 * @author Arnaud Urbain
 */

public class User {

    /**
     * Parameters.
     */
    private int id;
    private String first_name;
    private String name;
    private String mail;
    private String pwd;
    private String rights;

    /**
     * Constructor.
     */
    public User(){}

    /**
     * Constructor.
     *
     * @param first_name first_name of the user
     * @param name name of the user
     * @param mail mail of the user
     * @param pwd password of the user
     */
    public User(String first_name, String name, String mail, String pwd){
        this.first_name = first_name;
        this.name = name;
        this.mail = mail;
        this.pwd = pwd;
        this.rights = "basic";
    }

    /**
     * Gets the id of the user.
     *
     * @return id of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the user.
     *
     * @param id new id for the user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the first name of the user.
     *
     * @return first name of the user
     */
    public String getFirstName() {
        return first_name;
    }

    /**
     * Sets the first name of the user.
     *
     * @param first_name new first name for the user
     */
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Gets the name of the user.
     *
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name new name for the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the mail address of the user.
     *
     * @return mail address of the user
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the mail address of the user.
     *
     * @param mail new mail address for the user
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Gets the password of the user.
     *
     * @return password of the user
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Sets the password of the user.
     *
     * @param pwd new password for the user
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Gets rights of the user.
     *
     * @return rights of the user
     */
    public String getRights() {
        return rights;
    }

    /**
     * Sets the rights of the user.
     *
     * @param rights new rights for the user
     */
    public void setRights(String rights) {
        this.rights = rights;
    }

    /**
     * Create a string with the user's parameters.
     *
     * @return a string corresponding to the user
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", pwd='" + pwd + '\'' +
                ", rights='" + rights + '\'' +
                '}';
    }
}