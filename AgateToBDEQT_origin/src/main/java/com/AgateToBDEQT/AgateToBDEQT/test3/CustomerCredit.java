package com.AgateToBDEQT.AgateToBDEQT.test3;

public class CustomerCredit {
    public CustomerCredit() {
    }

    public CustomerCredit(int id, String name, int credit) {
        this.id = id;
        this.name = name;
        this.credit = credit;
    }

    private int id;
    private String name;
    private int credit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
//getters and setters removed for clarity
}