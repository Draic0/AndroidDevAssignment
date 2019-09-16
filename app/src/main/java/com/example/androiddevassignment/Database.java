package com.example.androiddevassignment;

public class Database {
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
