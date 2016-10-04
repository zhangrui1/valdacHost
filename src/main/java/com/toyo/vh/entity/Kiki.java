package com.toyo.vh.entity;

import com.toyo.vh.dto.KikiForm;

/**
 * Created by Lsr on 10/15/14.
 * 機器エンティティ
 */
public class Kiki {

    public int kikiId;
    public int kikisysidKiki;
    public String kikiBunrui;
    public String kikiBunruiSeq;
    public String kikiNo;
    public String kikiMei;
    public String syukan;
    public String makerRyaku;
    public String maker;
    public String katasikiNo;
    public String serialNo;
    public String orderNo;
    public String bikou;
    public String imageId;
    public String trkDate;
    public String updDate;
    public String kikiDelFlg;

    /**
     * kikiFormからkikiに変換
     * */
    public void makeupValveByForm(KikiForm kikiForm){
        setKikiBunrui(kikiForm.getKikiBunrui());
        setKikiBunruiSeq(kikiForm.getKikiBunruiSeq());
        setKikiNo(kikiForm.getKikiNo());
        setKikiMei(kikiForm.getKikiMei());
        setSyukan(kikiForm.getSyukan());
        setMakerRyaku(kikiForm.getMakerRyaku());
        setMaker(kikiForm.getMaker());
        setKatasikiNo(kikiForm.getKatasikiNo());
        setSerialNo(kikiForm.getSerialNo());
        setOrderNo(kikiForm.getOrderNo());
        setBikou(kikiForm.getBikou());
        setImageId(kikiForm.getImageId());
        setTrkDate(kikiForm.getTrkDate());
        setUpdDate(kikiForm.getUpdDate());
    }

    //lucene初期化時使う
    public String toText(){
        String text =
                    kikiId+"\t,"+
                    kikiBunrui+"\t,"+
                    kikiBunruiSeq+"\t,"+
                    kikiNo+"\t,"+
                    kikiMei+"\t,"+
                    syukan+"\t,"+
                    makerRyaku+"\t,"+
                    maker+"\t,"+
                    katasikiNo+"\t,"+
                    serialNo+"\t,"+
                    orderNo+"\t,"+
                    bikou+"\t,"+
                    imageId+"\t,"+
                    trkDate+"\t,"+
                    updDate+"\t,"+
                    kikiDelFlg+"\t,"+
                    kikisysidKiki+"\t,"+"End"+"\t,"+"KI"+kikiId+"End"+"\t,"+"VA"+kikisysidKiki+"End";
        return text;
    }
    //luceneからKikiに戻る時使う
    public Kiki toKiki(String bodyText){
        Kiki kiki=new Kiki();

        String words[] = bodyText.split("\t,");
        kiki.setKikiId(Integer.parseInt(words[0]));
        kiki.setKikiBunrui(words[1]);
        kiki.setKikiBunruiSeq(words[2]);
        kiki.setKikiNo(words[3]);
        kiki.setKikiMei(words[4]);
        kiki.setSyukan(words[5]);
        kiki.setMakerRyaku(words[6]);
        kiki.setMaker(words[7]);
        kiki.setKatasikiNo(words[8]);
        kiki.setSerialNo(words[9]);
        kiki.setOrderNo(words[10]);
        kiki.setBikou(words[11]);
        kiki.setImageId(words[12]);
        kiki.setTrkDate(words[13]);
        kiki.setUpdDate(words[14]);
        kiki.setKikiDelFlg(words[15]);
        kiki.setKikisysidKiki(Integer.parseInt(words[16]));
        return kiki;
    }

    public int getKikiId() {
        return kikiId;
    }

    public void setKikiId(int kikiId) {
        this.kikiId = kikiId;
    }

    public int getKikisysidKiki() {
        return kikisysidKiki;
    }

    public void setKikisysidKiki(int kikisysidKiki) {
        this.kikisysidKiki = kikisysidKiki;
    }

    public String getKikiBunrui() {
        return kikiBunrui;
    }

    public void setKikiBunrui(String kikiBunrui) {
        this.kikiBunrui = kikiBunrui;
    }

    public String getKikiBunruiSeq() {
        return kikiBunruiSeq;
    }

    public void setKikiBunruiSeq(String kikiBunruiSeq) {
        this.kikiBunruiSeq = kikiBunruiSeq;
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

    public String getBikou() {
        return bikou;
    }

    public void setBikou(String bikou) {
        this.bikou = bikou;
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

    public String getKikiDelFlg() {
        return kikiDelFlg;
    }

    public void setKikiDelFlg(String kikiDelFlg) {
        this.kikiDelFlg = kikiDelFlg;
    }

}
