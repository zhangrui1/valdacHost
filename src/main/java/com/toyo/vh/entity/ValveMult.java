package com.toyo.vh.entity;

import com.toyo.vh.dto.ValveMultForm;

/**
 * Created by zhangrui on 2016/03/28.
 * 弁エンティティ
 */
public class ValveMult {
    //弁項目
    public int kikiSysId;
    public String locationName;
    public String kCode;
    public String kikiSysSeq;
    public String vNo;
    public String vNoSub;
    public String benMeisyo;
    public String setBasyo;
    public String setSetubi;
    public String setKiki;
    public String keitou;
    public String kougu1M;
    public String kougu2S;
    public String kougu3T;
    public String kougu4L;
    public String kougu5O;
    public String keisikiRyaku;
    public String keisiki;
    public String sousaRyaku;
    public String sousa;
    public String classRyaku;
    public String classType;
    public String yobikeiRyaku;
    public String yobikei;
    public String szHouRyaku;
    public String szHou;
    public String szKikaku;
    public String zaisituRyaku;
    public String zaisitu;
    public String aturyokuMax;
    public String tani;
    public String ondoMax;
    public String ryutaiRyaku;
    public String ryutai;
    public String ics;
    public String futai;
    public String trkDate;
    public String updDate;

    //機器項目
    public String kikiBunrui;
    public String kikiNo;
    public String kikiMei;
    public String syukan;
    public String makerRyaku;
    public String maker;
    public String katasikiNo;
    public String serialNo;
    public String orderNo;

    //部品項目
    public String buhinKbn;
    public String asbKbn;
    public String siyouKasyo;
    public String buhinMei;
    public String hyojunSiyou;
    public String sunpou;
    public String suryo;
    public String sizaiName;
    public String hinban;
    public String buhinMakerRyaku;
    public String buhinMaker;

    /**
     * ValveFormからvalveに変換
     * */
    public Valve makeupValveByForm(ValveMultForm valveMultForm) {
        Valve valve=new Valve();
        valve.setvNo(valveMultForm.getvNo());
        valve.setLocationName(valveMultForm.getLocationName());
        valve.setvNoSub(valveMultForm.getvNoSub());
        valve.setBenMeisyo(valveMultForm.getBenMeisyo());
        valve.setSetBasyo(valveMultForm.getSetBasyo());
        valve.setSetKiki(valveMultForm.getSetKiki());
        valve.setSetSetubi(valveMultForm.getSetSetubi());
        valve.setKeitou(valveMultForm.getKeitou());
        valve.setKougu1M(valveMultForm.getKougu1M());
        valve.setKougu2S(valveMultForm.getKougu2S());
        valve.setKougu3T(valveMultForm.getKougu3T());
        valve.setKougu4L(valveMultForm.getKougu4L());
        valve.setKougu5O(valveMultForm.getKougu5O());
        valve.setKeisikiRyaku(valveMultForm.getKeisikiRyaku());
        valve.setKeisiki(valveMultForm.getKeisiki());
        valve.setSousaRyaku(valveMultForm.getSousaRyaku());
        valve.setSousa(valveMultForm.getSousa());
        valve.setClassRyaku(valveMultForm.getClassRyaku());
        valve.setClassType(valveMultForm.getClassType());
        valve.setYobikei(valveMultForm.getYobikei());
        valve.setYobikeiRyaku(valveMultForm.getYobikeiRyaku());
        valve.setSzHou(valveMultForm.getSzHou());
        valve.setSzHouRyaku(valveMultForm.getSzHouRyaku());
        valve.setSzKikaku(valveMultForm.getSzKikaku());
        valve.setZaisitu(valveMultForm.getZaisitu());
        valve.setZaisituRyaku(valveMultForm.getZaisituRyaku());
        valve.setAturyokuMax(valveMultForm.getAturyokuMax());
        valve.setTani(valveMultForm.getTani());
        valve.setOndoMax(valveMultForm.getOndoMax());
        valve.setRyutai(valveMultForm.getRyutai());
        valve.setRyutaiRyaku(valveMultForm.getRyutaiRyaku());
        valve.setIcs(valveMultForm.getIcs());
        valve.setFutai(valveMultForm.getFutai());

        return valve;
     }

    public Kiki makeupKikiByForm(ValveMultForm valveMultForm) {
        Kiki kiki=new Kiki();
        kiki.setKikiBunrui("");
        kiki.setKikiNo("");
        kiki.setKikiMei("");
        kiki.setSyukan(valveMultForm.getSyukan());
        kiki.setMakerRyaku(valveMultForm.getMakerRyaku());
        kiki.setMaker(valveMultForm.getMaker());
        kiki.setKatasikiNo(valveMultForm.getKatasikiNo());
        kiki.setSerialNo("");
        kiki.setOrderNo("");
        kiki.setBikou(valveMultForm.getLocationName());

        return kiki;
    }

    public Buhin makeupBuhinByForm(ValveMultForm valveMultForm) {
        Buhin buhin=new Buhin();
        buhin.setBuhinKbn("");
        buhin.setAsbKbn("");
        buhin.setSiyouKasyo("");
        buhin.setBuhinMei(valveMultForm.getBuhinMei());
        buhin.setHyojunSiyou(valveMultForm.getHyojunSiyou());
        buhin.setSunpou(valveMultForm.getSunpou());
        buhin.setSuryo("");
        buhin.setSizaiName("");
        buhin.setHinban("");
        buhin.setMakerRyaku(valveMultForm.getBuhinMakerRyaku());
        buhin.setMaker(valveMultForm.getBuhinMaker());
        buhin.setBikou(valveMultForm.getLocationName());
        return buhin;
    }

    /**
     * ValveFormからvalveに変換
     * */
    public ValveMult makeupValveMultByForm(ValveMultForm valveMultForm) {
        ValveMult valveMult=new ValveMult();
        valveMult.setvNo(valveMultForm.getvNo());
        valveMult.setLocationName(valveMultForm.getLocationName());
        valveMult.setvNoSub(valveMultForm.getvNoSub());
        valveMult.setBenMeisyo(valveMultForm.getBenMeisyo());
        valveMult.setSetBasyo(valveMultForm.getSetBasyo());
        valveMult.setSetKiki(valveMultForm.getSetKiki());
        valveMult.setSetSetubi(valveMultForm.getSetSetubi());
        valveMult.setKeitou(valveMultForm.getKeitou());
        valveMult.setKougu1M(valveMultForm.getKougu1M());
        valveMult.setKougu2S(valveMultForm.getKougu2S());
        valveMult.setKougu3T(valveMultForm.getKougu3T());
        valveMult.setKougu4L(valveMultForm.getKougu4L());
        valveMult.setKougu5O(valveMultForm.getKougu5O());
        valveMult.setKeisikiRyaku(valveMultForm.getKeisikiRyaku());
        valveMult.setKeisiki(valveMultForm.getKeisiki());
        valveMult.setSousaRyaku(valveMultForm.getSousaRyaku());
        valveMult.setSousa(valveMultForm.getSousa());
        valveMult.setClassRyaku(valveMultForm.getClassRyaku());
        valveMult.setClassType(valveMultForm.getClassType());
        valveMult.setYobikei(valveMultForm.getYobikei());
        valveMult.setYobikeiRyaku(valveMultForm.getYobikeiRyaku());
        valveMult.setSzHou(valveMultForm.getSzHou());
        valveMult.setSzHouRyaku(valveMultForm.getSzHouRyaku());
        valveMult.setSzKikaku(valveMultForm.getSzKikaku());
        valveMult.setZaisitu(valveMultForm.getZaisitu());
        valveMult.setZaisituRyaku(valveMultForm.getZaisituRyaku());
        valveMult.setAturyokuMax(valveMultForm.getAturyokuMax());
        valveMult.setTani(valveMultForm.getTani());
        valveMult.setOndoMax(valveMultForm.getOndoMax());
        valveMult.setRyutai(valveMultForm.getRyutai());
        valveMult.setRyutaiRyaku(valveMultForm.getRyutaiRyaku());
        valveMult.setIcs(valveMultForm.getIcs());
        valveMult.setFutai(valveMultForm.getFutai());

        valveMult.setKikiBunrui(valveMultForm.getKikiBunrui());
        valveMult.setKikiNo(valveMultForm.getKikiNo());
        valveMult.setKikiMei(valveMultForm.getKikiMei());
        valveMult.setSyukan(valveMultForm.getSyukan());
        valveMult.setMakerRyaku(valveMultForm.getMakerRyaku());
        valveMult.setMaker(valveMultForm.getMaker());
        valveMult.setKatasikiNo(valveMultForm.getKatasikiNo());
        valveMult.setSerialNo(valveMultForm.getSerialNo());
        valveMult.setOrderNo(valveMultForm.getOrderNo());


        valveMult.setBuhinKbn(valveMultForm.getBuhinKbn());
        valveMult.setAsbKbn(valveMultForm.getAsbKbn());
        valveMult.setSiyouKasyo(valveMultForm.getSiyouKasyo());
        valveMult.setBuhinMei(valveMultForm.getBuhinMei());
        valveMult.setHyojunSiyou(valveMultForm.getHyojunSiyou());
        valveMult.setSunpou(valveMultForm.getSunpou());
        valveMult.setSuryo(valveMultForm.getSuryo());
        valveMult.setSizaiName(valveMultForm.getSizaiName());
        valveMult.setHinban(valveMultForm.getHinban());
        valveMult.setBuhinMakerRyaku(valveMultForm.getBuhinMakerRyaku());
        valveMult.setBuhinMaker(valveMultForm.getBuhinMaker());

        return valveMult;
    }



    public int getKikiSysId() {
        return kikiSysId;
    }

    public void setKikiSysId(int kikiSysId) {
        this.kikiSysId = kikiSysId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getkCode() {
        return kCode;
    }

    public void setkCode(String kCode) {
        this.kCode = kCode;
    }

    public String getKikiSysSeq() {
        return kikiSysSeq;
    }

    public void setKikiSysSeq(String kikiSysSeq) {
        this.kikiSysSeq = kikiSysSeq;
    }

    public String getvNo() {
        return vNo;
    }

    public void setvNo(String vNo) {
        this.vNo = vNo;
    }

    public String getvNoSub() {
        return vNoSub;
    }

    public void setvNoSub(String vNoSub) {
        this.vNoSub = vNoSub;
    }

    public String getBenMeisyo() {
        return benMeisyo;
    }

    public void setBenMeisyo(String benMeisyo) {
        this.benMeisyo = benMeisyo;
    }

    public String getSetBasyo(){return  setBasyo;}

    public void setSetBasyo(String setBasyo){this.setBasyo=setBasyo;}

    public String getSetSetubi(){return  setSetubi;}

    public void setSetSetubi(String setSetubi){this.setSetubi=setSetubi;}

    public String getSetKiki() {return setKiki;}

    public void setSetKiki(String setKiki) { this.setKiki = setKiki;}

    public String getKeitou() {return keitou;}

    public void setKeitou(String keitou) {this.keitou = keitou;}

    public String getKougu1M() {return kougu1M;}

    public void setKougu1M(String kougu1M) {this.kougu1M = kougu1M;}

    public String getKougu2S() {return kougu2S;}

    public void setKougu2S(String kougu2S) {this.kougu2S = kougu2S;}

    public String getKougu3T() {return kougu3T;}

    public void setKougu3T(String kougu3T) {this.kougu3T = kougu3T;}

    public String getKougu4L() {return kougu4L;}

    public void setKougu4L(String kougu4L) {this.kougu4L = kougu4L;}

    public String getKougu5O() {return kougu5O;}

    public void setKougu5O(String kougu5O) {this.kougu5O = kougu5O;}

    public String getKeisikiRyaku() {
        return keisikiRyaku;
    }

    public void setKeisikiRyaku(String keisikiRyaku) {
        this.keisikiRyaku = keisikiRyaku;
    }

    public String getKeisiki() {
        return keisiki;
    }

    public void setKeisiki(String keisiki) {
        this.keisiki = keisiki;
    }

    public String getSousaRyaku() {
        return sousaRyaku;
    }

    public void setSousaRyaku(String sousaRyaku) {
        this.sousaRyaku = sousaRyaku;
    }

    public String getSousa() {
        return sousa;
    }

    public void setSousa(String sousa) {
        this.sousa = sousa;
    }

    public String getClassRyaku() {
        return classRyaku;
    }

    public void setClassRyaku(String classRyaku) {
        this.classRyaku = classRyaku;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getYobikeiRyaku() {
        return yobikeiRyaku;
    }

    public void setYobikeiRyaku(String yobikeiRyaku) {
        this.yobikeiRyaku = yobikeiRyaku;
    }

    public String getYobikei() {
        return yobikei;
    }

    public void setYobikei(String yobikei) {
        this.yobikei = yobikei;
    }

    public String getSzHouRyaku() {
        return szHouRyaku;
    }

    public void setSzHouRyaku(String szHouRyaku) {
        this.szHouRyaku = szHouRyaku;
    }

    public String getSzHou() {
        return szHou;
    }

    public void setSzHou(String szHou) {
        this.szHou = szHou;
    }

    public String getSzKikaku() {
        return szKikaku;
    }

    public void setSzKikaku(String szKikaku) {
        this.szKikaku = szKikaku;
    }

    public String getZaisituRyaku() {
        return zaisituRyaku;
    }

    public void setZaisituRyaku(String zaisituRyaku) {
        this.zaisituRyaku = zaisituRyaku;
    }

    public String getZaisitu() {
        return zaisitu;
    }

    public void setZaisitu(String zaisitu) {
        this.zaisitu = zaisitu;
    }

    public String getAturyokuMax() {
        return aturyokuMax;
    }

    public void setAturyokuMax(String aturyokuMax) {
        this.aturyokuMax = aturyokuMax;
    }

    public String getTani() {
        return tani;
    }

    public void setTani(String tani) {
        this.tani = tani;
    }

    public String getOndoMax() {
        return ondoMax;
    }

    public void setOndoMax(String ondoMax) {
        this.ondoMax = ondoMax;
    }

    public String getRyutaiRyaku() {
        return ryutaiRyaku;
    }

    public void setRyutaiRyaku(String ryutaiRyaku) {
        this.ryutaiRyaku = ryutaiRyaku;
    }

    public String getRyutai() {
        return ryutai;
    }

    public void setRyutai(String ryutai) {
        this.ryutai = ryutai;
    }

    public String getIcs() {
        return ics;
    }

    public void setIcs(String ics) {
        this.ics = ics;
    }

    public String getFutai() {
        return futai;
    }

    public void setFutai(String futai) {
        this.futai = futai;
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

    //    機器部分
    public String getKikiBunrui() { return kikiBunrui;}

    public void setKikiBunrui(String kikiBunrui) {
        this.kikiBunrui = kikiBunrui;
    }

    public String getKikiNo() {
        return kikiNo;
    }

    public void setKikiNo(String kikiNo) {
        this.kikiNo = kikiNo;
    }

    public String getKikiMei() {
        return kikiMei;
    }

    public void setKikiMei(String kikiMei) {
        this.kikiMei = kikiMei;
    }

    public String getSyukan() {
        return syukan;
    }

    public void setSyukan(String syukan) {
        this.syukan = syukan;
    }

    public String getMakerRyaku() {
        return makerRyaku;
    }

    public void setMakerRyaku(String makerRyaku) {
        this.makerRyaku = makerRyaku;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getKatasikiNo() {
        return katasikiNo;
    }

    public void setKatasikiNo(String katasikiNo) {
        this.katasikiNo = katasikiNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    //部品
    public String getBuhinKbn() {
        return buhinKbn;
    }

    public void setBuhinKbn(String buhinKbn) {
        this.buhinKbn = buhinKbn;
    }

    public String getSiyouKasyo() {
        return siyouKasyo;
    }

    public void setSiyouKasyo(String siyouKasyo) {
        this.siyouKasyo = siyouKasyo;
    }

    public String getAsbKbn() {
        return asbKbn;
    }

    public void setAsbKbn(String asbKbn) {
        this.asbKbn = asbKbn;
    }

    public String getBuhinMei() {
        return buhinMei;
    }

    public void setBuhinMei(String buhinMei) {
        this.buhinMei = buhinMei;
    }

    public String getHyojunSiyou() {
        return hyojunSiyou;
    }

    public void setHyojunSiyou(String hyojunSiyou) {
        this.hyojunSiyou = hyojunSiyou;
    }

    public String getBuhinMakerRyaku() {
        return buhinMakerRyaku;
    }

    public void setBuhinMakerRyaku(String buhinMakerRyaku) {
        this.buhinMakerRyaku = buhinMakerRyaku;
    }

    public String getBuhinMaker() {
        return buhinMaker;
    }

    public void setBuhinMaker(String buhinMaker) {
        this.buhinMaker = buhinMaker;
    }

    public String getSunpou() {
        return sunpou;
    }

    public void setSunpou(String sunpou) {
        this.sunpou = sunpou;
    }

    public String getSuryo() {
        return suryo;
    }

    public void setSuryo(String suryo) {
        this.suryo = suryo;
    }

    public String getSizaiName() {
        return sizaiName;
    }

    public void setSizaiName(String sizaiName) {
        this.sizaiName = sizaiName;
    }

    public String getHinban() {
        return hinban;
    }

    public void setHinban(String hinban) {
        this.hinban = hinban;
    }

}
