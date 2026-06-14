package com.ch.ebusiness.entity;

public class Order {
    private Integer id;
    private Integer buserable_id;
    private double amount;
    private int status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBuserable_id() {
        return buserable_id;
    }

    public void setBuserable_id(Integer buserable_id) {
        this.buserable_id = buserable_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
