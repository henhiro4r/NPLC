package com.uc.nplc.model;

import java.io.Serializable;

public class History implements Serializable {

    public History(){}

    private String id;
    private String idPos;
    private String hMy;
    private String idVs;
    private String hVs;
    private String created;

    public History(String id, String idPos, String hMy, String idVs, String hVs, String created) {
        this.id = id;
        this.idPos = idPos;
        this.hMy = hMy;
        this.idVs = idVs;
        this.hVs = hVs;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public String getIdPos() {
        return idPos;
    }

    public String gethMy() {
        return hMy;
    }

    public String getIdVs() {
        return idVs;
    }

    public String gethVs() {
        return hVs;
    }

    public String getCreated() {
        return created;
    }
}
