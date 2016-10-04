package com.toyo.vh.entity;

import com.toyo.vh.dto.ValveForm;

/**
 * Created by Lsr on 10/14/14.
 * 弁エンティティ
 */
public class Valve {
    public int kikiSysId;
    public String locationName;
    public String kCode;
    public String kikiSysSeq;
    public String vNo;
    public String vNoSub;
    public String benMeisyo;
    public String setSetubi;
    public String setBasyo;
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
    public String delFlg;
    public String kenanFlg;
    public String gpFlg;
    public String trkDate;
    public String updDate;

    /**
     * ValveFormからvalveに変換
     * */
    public void makeupValveByForm(ValveForm valveForm) {
        setvNo(valveForm.getvNo());
        setLocationName(valveForm.getLocationName());
        setvNoSub(valveForm.getvNoSub());
        setBenMeisyo(valveForm.getBenMeisyo());
        setSetBasyo(valveForm.getSetBasyo());
        setSetKiki(valveForm.getSetKiki());
        setSetSetubi(valveForm.getSetSetubi());
        setKeitou(valveForm.getKeitou());
        setKougu1M(valveForm.getKougu1M());
        setKougu2S(valveForm.getKougu2S());
        setKougu3T(valveForm.getKougu3T());
        setKougu4L(valveForm.getKougu4L());
        setKougu5O(valveForm.getKougu5O());
        setKeisikiRyaku(valveForm.getKeisikiRyaku());
        setKeisiki(valveForm.getKeisiki());
        setSousaRyaku(valveForm.getSousaRyaku());
        setSousa(valveForm.getSousa());
        setClassRyaku(valveForm.getClassRyaku());
        setClassType(valveForm.getClassType());
        setYobikei(valveForm.getYobikei());
        setYobikeiRyaku(valveForm.getYobikeiRyaku());
        setSzHou(valveForm.getSzHou());
        setSzHouRyaku(valveForm.getSzHouRyaku());
        setSzKikaku(valveForm.getSzKikaku());
        setZaisitu(valveForm.getZaisitu());
        setZaisituRyaku(valveForm.getZaisituRyaku());
        setAturyokuMax(valveForm.getAturyokuMax());
        setTani(valveForm.getTani());
        setOndoMax(valveForm.getOndoMax());
        setRyutai(valveForm.getRyutai());
        setRyutaiRyaku(valveForm.getRyutaiRyaku());
        setIcs(valveForm.getIcs());
        setFutai(valveForm.getFutai());
    }

    //lucene初期化時使う
    public String toText(){
        String text =
                    kikiSysId+"\t,"+
                    locationName+"\t,"+
                    kCode+"\t,"+
                    kikiSysSeq+"\t,"+
                    vNo+"\t,"+
                    vNoSub+"\t,"+
                    benMeisyo+"\t,"+
                    setBasyo+"\t,"+
                    setKiki+"\t,"+
                    setSetubi+"\t,"+
                    keitou+"\t,"+
                    kougu1M+"\t,"+
                    kougu2S+"\t,"+
                    kougu3T+"\t,"+
                    kougu4L+"\t,"+
                    kougu5O+"\t,"+
                    keisikiRyaku+"\t,"+
                    keisiki+"\t,"+
                    sousaRyaku+"\t,"+
                    sousa+"\t,"+
                    classRyaku+"\t,"+
                    classType+"\t,"+
                    yobikeiRyaku+"\t,"+
                    yobikei+"\t,"+
                    szHouRyaku+"\t,"+
                    szHou+"\t,"+
                    szKikaku+"\t,"+
                    zaisituRyaku+"\t,"+
                    zaisitu+"\t,"+
                    aturyokuMax+"\t,"+
                    tani+"\t,"+
                    ondoMax+"\t,"+
                    ryutaiRyaku+"\t,"+
                    ryutai+"\t,"+
                    ics+"\t,"+
                    futai+"\t,"+
                    delFlg+"\t,"+
                    kenanFlg+"\t,"+
                    gpFlg+"\t,"+
                    trkDate+"\t,"+
                    updDate+"\t,"+"End"+"\t,"+"VA"+kikiSysId+"End";
        return text;
    }

    //luceneからValveに戻る時使う
    public Valve toValve(String bodyText){
        Valve valve=new Valve();
        String words[] = bodyText.split("\t,");

        valve.setKikiSysId(Integer.parseInt(words[0]));
        valve.setLocationName(words[1]);
        valve.setkCode(words[2]);
        valve.setKikiSysSeq(words[3]);
        valve.setvNo(words[4]);
        valve.setvNoSub(words[5]);
        valve.setBenMeisyo(words[6]);
        valve.setSetBasyo(words[7]);
        valve.setSetKiki(words[8]);
        valve.setSetSetubi(words[9]);
        valve.setKeitou(words[10]);
        valve.setKougu1M(words[11]);
        valve.setKougu2S(words[12]);
        valve.setKougu3T(words[13]);
        valve.setKougu4L(words[14]);
        valve.setKougu5O(words[15]);
        valve.setKeisikiRyaku(words[16]);
        valve.setKeisiki(words[17]);
        valve.setSousaRyaku(words[18]);
        valve.setSousa(words[19]);
        valve.setClassRyaku(words[20]);
        valve.setClassType(words[21]);
        valve.setYobikeiRyaku(words[22]);
        valve.setYobikei(words[23]);
        valve.setSzHouRyaku(words[24]);
        valve.setSzHou(words[25]);
        valve.setSzKikaku(words[26]);
        valve.setZaisituRyaku(words[27]);
        valve.setZaisitu(words[28]);
        valve.setAturyokuMax(words[29]);
        valve.setTani(words[30]);
        valve.setOndoMax(words[31]);
        valve.setRyutaiRyaku(words[32]);
        valve.setRyutai(words[33]);
        valve.setIcs(words[34]);
        valve.setFutai(words[35]);
        valve.setDelFlg(words[36]);
        valve.setKenanFlg(words[37]);
        valve.setGpFlg(words[38]);
        valve.setTrkDate(words[39]);
        valve.setUpdDate(words[40]);

        return valve;
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

    public String getKenanFlg() {
        return kenanFlg;
    }

    public void setKenanFlg(String kenanFlg) {
        this.kenanFlg = kenanFlg;
    }

    public String getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(String delFlg) {
        this.delFlg = delFlg;
    }

    public String getGpFlg() {
        return gpFlg;
    }

    public void setGpFlg(String gpFlg) {
        this.gpFlg = gpFlg;
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
