package com.uccendigital.secure.elements;

public class Sim {

    String name, serial;

    public Sim(String name, String serial) {
        this.name = name;
        this.serial = serial;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public String getSerial() {
        return serial;
    }

}
