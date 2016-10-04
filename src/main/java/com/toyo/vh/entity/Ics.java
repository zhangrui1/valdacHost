package com.toyo.vh.entity;
/**
 * Created by zhangrui1 on 2/4/15.
 * 画像エンティティ
 */
public class Ics {

    public int id;
    public String icsNum;
    public String bikou;
    public String imagepath;
    public String trkDate;
    public String updDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcsNum() {
        return icsNum;
    }

    public void setIcsNum(String icsNum) {
        this.icsNum = icsNum;
    }

    public String getBikou() { return bikou; }

    public void setBikou(String bikou) { this.bikou = bikou; }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getTrkDate() {
        return trkDate;
    }

    public void setTrkDate(String trkDate) {
        this.trkDate = trkDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }
}
