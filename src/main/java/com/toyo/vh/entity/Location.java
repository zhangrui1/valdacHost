package com.toyo.vh.entity;

/**
 * Created by zhangrui on 14/11/12.
 * 会社名エンティティ
 */
public class Location {
    public int id;
    public String kCodeL;
    public String kCodeM;
    public String kCodeS;

    public String kCodeLKana;
    public String kCodeMKana;
    public String kCodeSKana;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getkCodeL() {
        return kCodeL;
    }

    public void setkCodeL(String kCodeL) {
        this.kCodeL = kCodeL;
    }

    public String getkCodeM() {
        return kCodeM;
    }

    public void setkCodeM(String kCodeM) {
        this.kCodeM = kCodeM;
    }

    public String getkCodeS() {
        return kCodeS;
    }

    public void setkCodeS(String kCodeS) {
        this.kCodeS = kCodeS;
    }

    public String getkCodeLKana(){return  kCodeLKana;}
    public void setkCodeLKana(String kCodeLKana){this.kCodeLKana=kCodeLKana;}

    public String getkCodeMKana(){return  kCodeMKana;}
    public void setkCodeMKana(String kCodeMKana){this.kCodeMKana=kCodeMKana;}

    public String getkCodeSKana(){return  kCodeSKana;}
    public void setkCodeSKana(String kCodeSKana){this.kCodeSKana=kCodeSKana;}
}
