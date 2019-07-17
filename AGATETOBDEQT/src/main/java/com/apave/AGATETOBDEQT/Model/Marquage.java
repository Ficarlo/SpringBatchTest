package com.apave.AGATETOBDEQT.Model;

public class Marquage {

    private int log_id;
    private int person_id;

    public Marquage(int log_id, int person_id) {
        this.log_id = log_id;
        this.person_id = person_id;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    @Override
    public String toString() {
        return "log_id: " + log_id + ", person_id: " + person_id;
    }
}
