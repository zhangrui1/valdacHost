package com.toyo.vh.entity;

/**
 * Created by Lsr on 11/5/14.
 * 画像と機器または部品関係のエンティティ
 */
public class Imagepart {

    public int id;
    public int partid;
    public String imagename;
    public int kikiSysId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartid() {
        return partid;
    }

    public void setPartid(int partid) {
        this.partid = partid;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public int getKikiSysId(){return kikiSysId;}

    public void setKikiSysId(int kikiSysId){this.kikiSysId=kikiSysId;}
}
