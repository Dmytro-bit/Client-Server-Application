package org.example.DTOs;

public class Table {
    private int id;
    private int capacity;

    public Table(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}