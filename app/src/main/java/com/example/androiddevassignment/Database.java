package com.example.androiddevassignment;

import java.io.Serializable;

public class Database implements Serializable {
    private String id, name;
    public Database(String id, String name){
        this.id = id;
        this.name = name;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    @Override
    public String toString() {
        return name;
    }
}
