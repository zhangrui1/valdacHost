package com.toyo.vh.entity;

import com.toyo.vh.dto.BuhinForm;

/**
 * Created by Lsr on 10/18/14.
 * 部品エンティティ
 */
public class Buhin {
    public int buhinId;
    public int kikisysidBuhin;
    public int kikiidBuhin;
    public String buhinKbn;
    public String BuhinSeq;
    public String asbKbn;
    public String buhinzuBikou;
    public String buhinMei;
    public String hyojunSiyou;
    public String siyouKasyo;
    public String makerRyaku;
    public String sizaiName;
    public String maker;
    public String sunpou;
    public String sunpouL;
    public String sunpouW;
    public String sunpouH;
    public String sunpouO;
    public String hinban;
    public String bikou;
    public String suryo;
    public String imageId;
    public String buhinStatus;
    public String trkDate;
    public String updDate;//hinban

    /**
     * buhinFormからbuhinに変換
     * */
    public void makeupValueByForm(BuhinForm buhinForm){
        setBuhinKbn(buhinForm.getBuhinKbn());
        setBuhinSeq(buhinForm.getBuhinSeq());
        setAsbKbn(buhinForm.getAsbKbn());
        setBuhinzuBikou(buhinForm.getBuhinzuBikou());
        setBuhinMei(buhinForm.getBuhinMei());
        setHyojunSiyou(buhinForm.getHyojunSiyou());
        setMakerRyaku(buhinForm.getMakerRyaku());
        setMaker(buhinForm.getMaker());
        setSunpouL(buhinForm.getSunpouL());
        setSunpouW(buhinForm.getSunpouW());
        setSunpouH(buhinForm.getSunpouH());
        setSunpouO(buhinForm.getSunpouO());
        setBikou(buhinForm.getBikou());
        setSuryo(buhinForm.getSuryo());
        setSizaiName(buhinForm.getSizaiName());
        setSiyouKasyo(buhinForm.getSiyouKasyo());
        setImageId(buhinForm.getImageId());
        setTrkDate(buhinForm.getTrkDate());
        setUpdDate(buhinForm.getUpdDate());
        setHinban(buhinForm.getHinban());
        setBuhinStatus(buhinForm.getBuhinStatus());
    }

    //lucene初期化時使う
    public String toText(){
        String text =
                buhinId+"\t,"+
                buhinKbn+"\t,"+
                BuhinSeq+"\t,"+
                asbKbn+"\t,"+
                buhinzuBikou+"\t,"+
                buhinMei+"\t,"+
                hyojunSiyou+"\t,"+
                siyouKasyo+"\t,"+
                makerRyaku+"\t,"+
                sizaiName+"\t,"+
                maker+"\t,"+
                sunpou+"\t,"+
                sunpouL+"\t,"+
                sunpouW+"\t,"+
                sunpouH+"\t,"+
                sunpouO+"\t,"+
                hinban+"\t,"+
                bikou+"\t,"+
                suryo+"\t,"+
                imageId+"\t,"+
                buhinStatus+"\t,"+
                trkDate+"\t,"+
                updDate+"\t,"+
                kikisysidBuhin+"\t,"+
                kikiidBuhin+"\t,"+"End"+"\t,"+"BU"+buhinId+"End"+"\t,"+"VA"+kikisysidBuhin+"End"+"\t,"+"KI"+kikiidBuhin+"End";
        return text;
    }

    //luceneからBuhinに戻る時使う
    public Buhin toBuhin(String bodyText){
        Buhin buhin=new Buhin();
        String words[]=bodyText.split("\t,");

        buhin.setBuhinId(Integer.parseInt(words[0]));
        buhin.setBuhinKbn(words[1]);
        buhin.setBuhinSeq(words[2]);
        buhin.setAsbKbn(words[3]);
        buhin.setBuhinzuBikou(words[4]);
        buhin.setBuhinMei(words[5]);
        buhin.setHyojunSiyou(words[6]);
        buhin.setSiyouKasyo(words[7]);
        buhin.setMakerRyaku(words[8]);
        buhin.setSizaiName(words[9]);
        buhin.setMaker(words[10]);
        buhin.setSunpou(words[11]);
        buhin.setSunpouL(words[12]);
        buhin.setSunpouW(words[13]);
        buhin.setSunpouH(words[14]);
        buhin.setSunpouO(words[15]);
        buhin.setHinban(words[16]);
        buhin.setBikou(words[17]);
        buhin.setSuryo(words[18]);
        buhin.setImageId(words[19]);
        buhin.setBuhinStatus(words[20]);
        buhin.setTrkDate(words[21]);
        buhin.setUpdDate(words[22]);
        buhin.setKikisysidBuhin(Integer.parseInt(words[23]));
        buhin.setKikiidBuhin(Integer.parseInt(words[24]));
        return  buhin;
    }

    public int getTrkDateInt(){
        String intDate[] = trkDate.split("/");
        String result = "";
        for (int i = 0; i < intDate.length; i++) {
            result = result + intDate[i];
        }
        return Integer.valueOf(result);
    }
    public int getUpdDateInt(){
        String intDate[] = updDate.split("/");
        String result = "";
        for (int i = 0; i < intDate.length; i++) {
            result = result + intDate[i];
        }
        return Integer.valueOf(result);
    }

    public int getBuhinId() {
        return buhinId;
    }

    public void setBuhinId(int buhinId) {
        this.buhinId = buhinId;
    }

    public int getKikisysidBuhin() {
        return kikisysidBuhin;
    }

    public void setKikisysidBuhin(int kikisysidBuhin) {
        this.kikisysidBuhin = kikisysidBuhin;
    }

    public int getKikiidBuhin() {
        return kikiidBuhin;
    }

    public void setKikiidBuhin(int kikiidBuhin) {
        this.kikiidBuhin = kikiidBuhin;
    }

    public String getBuhinKbn() {
        return buhinKbn;
    }

    public void setBuhinKbn(String buhinKbn) {
        this.buhinKbn = buhinKbn;
    }

    public String getBuhinSeq() {
        return BuhinSeq;
    }

    public void setBuhinSeq(String buhinSeq) {
        BuhinSeq = buhinSeq;
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

    public String getBuhinzuBikou() {
        return buhinzuBikou;
    }

    public void setBuhinzuBikou(String buhinzuBikou) {
        this.buhinzuBikou = buhinzuBikou;
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

    public String getSunpou() {
        return sunpou;
    }

    public void setSunpou(String sunpou) {
        this.sunpou = sunpou;
    }

    public String getSunpouL() {
        return sunpouL;
    }

    public void setSunpouL(String sunpouL) {
        this.sunpouL = sunpouL;
    }

    public String getSunpouW() {
        return sunpouW;
    }

    public void setSunpouW(String sunpouW) {
        this.sunpouW = sunpouW;
    }

    public String getSunpouH() {
        return sunpouH;
    }

    public void setSunpouH(String sunpouH) {
        this.sunpouH = sunpouH;
    }

    public String getSunpouO() {
        return sunpouO;
    }

    public void setSunpouO(String sunpouO) {
        this.sunpouO = sunpouO;
    }

    public String getBikou() {
        return bikou;
    }

    public void setBikou(String bikou) {
        this.bikou = bikou;
    }

    public String getSuryo() {
        return suryo;
    }

    public void setSuryo(String suryo) {
        this.suryo = suryo;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
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

    public String getBuhinStatus() {
        return buhinStatus;
    }

    public void setBuhinStatus(String buhinStatus) {
        this.buhinStatus = buhinStatus;
    }

}
