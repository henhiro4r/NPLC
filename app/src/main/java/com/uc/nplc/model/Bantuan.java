package com.uc.nplc.model;

import java.io.Serializable;

public class Bantuan implements Serializable {

    public Bantuan(){}

    private String id;
    private String judul;
    private String des;
    private String updated;

    public Bantuan(String id, String judul, String des, String updated) {
        this.id = id;
        this.judul = judul;
        this.des = des;
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getDes() {
        return des;
    }

    public String getUpdated() {
        return updated;
    }
}
