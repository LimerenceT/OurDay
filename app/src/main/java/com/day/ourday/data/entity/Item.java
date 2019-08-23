package com.day.ourday.data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Create by LimerenceT on 2019-06-24
 */
@Entity
public class Item implements Serializable {
    public Item() {
    }

    @Ignore
    public Item(String name, String date) {
        this.name = name;
        this.date = date;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String date;

    private int days;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
