package com.toyo.vh.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangrui on 2015/04/09.
 */
public class ValveKenanDL {
    Valve valve;

    //弁に所属されてる機器リスト
    List<Kiki> kikiList;

    //機器の懸案
    Map<String,List<Kenan>> kenanList;

    //kenanIDと工事
    Map<String,Kouji> koujiMap;

    public Valve getValve(){return valve; }
    public void setValve(Valve valve){this.valve=valve;}

    public  List<Kiki> getKikiList(){return kikiList; }
    public void setKikiList( List<Kiki> kikiList){this.kikiList=kikiList;}

    public  Map<String,List<Kenan>>  getKenanList(){return kenanList; }
    public void setKenanList( Map<String,List<Kenan>> kenanList){this.kenanList=kenanList;}

    public Map<String,Kouji> getKoujiMap(){return  koujiMap;}
    public void setKoujiMap(Map<String,Kouji> koujiMap){this.koujiMap=koujiMap;}

}
