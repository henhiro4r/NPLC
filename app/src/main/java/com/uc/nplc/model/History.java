package com.uc.nplc.model;

import java.io.Serializable;

public class History implements Serializable {

    private String game_id;
    private String status;
    private String point;
    private String time_start;

    public History(String game_id, String status, String point, String time_start) {
        this.game_id = game_id;
        this.status = status;
        this.point = point;
        this.time_start = time_start;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }
}
