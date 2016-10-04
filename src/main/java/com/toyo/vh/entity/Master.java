package com.toyo.vh.entity;

/**
 * Created by zhangrui on 2014/10/15.
 * Masterエンティティ
 */
public class Master {

    /**マスターID*/
    public int masterId;
    /**マスター種別*/
    public String type;
    /**各種別の連番コード*/
    public String code;
    /**略称*/
    public String ryaku;
    /**名称*/
    public String name;

    /*** マスターIDの取得と設定*/
    public int getMasterId(){return masterId;}
    public void setMasterId(int masterId){
        this.masterId =masterId;
    }

    /*** マスター種別の取得と設定*/
    public String getType(){return type;}
    public void setType(String type){
        this.type=type;
    }

    /*** 各種別の連番コードの取得と設定*/
    public String getCode(){return code;}
    public void setCode(String code){
        this.code=code;
    }

    /*** 略称の取得と設定*/
    public String getRyaku(){return ryaku;}
    public void setRyaku(String ryaku){
        this.ryaku=ryaku;
    }

    /*** 名称の取得と設定*/
    public String getName(){return name;}
    public void setName(String name){
        this.name=name;
    }
}
