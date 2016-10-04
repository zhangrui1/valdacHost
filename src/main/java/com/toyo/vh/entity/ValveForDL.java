package com.toyo.vh.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangrui on 2015/04/09.
 */
public class ValveForDL {
    Valve valve;

    List<Kiki> kikiList;

    List<Buhin> buhins;

    Map<Kiki,List<Buhin>> buhinList;

    public Valve getValve(){return valve; }
    public void setValve(Valve valve){this.valve=valve;}

    public  List<Kiki> getKikiList(){return kikiList; }
    public void setKikiList( List<Kiki> kikiList){this.kikiList=kikiList;}

    public List<Buhin> getBuhins(){return  buhins;}
    public void setBuhins(List<Buhin> buhins){this.buhins=buhins;}

    public  Map<Kiki,List<Buhin>>  getBuhinList(){return buhinList; }
    public void setBuhinList( Map<Kiki,List<Buhin>> buhinList){this.buhinList=buhinList;}
}
