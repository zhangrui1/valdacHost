package com.toyo.vh.entity;


import java.util.List;

/**
 * Created by Lsr on 10/14/14.
 * 点検履歴エンティティ
 */
public class TenkenRirekiUtil{
    //tenkenkiki部分
    public int id;
    public int koujiId;
    public int koujirelationId;
    public String tenkenDate;
    public String tenkenNendo;
    public String tenkenRank;
    public String tenkennaiyo;
    public String tenkenkekka;
    public String tenkenBikou;
    public String kanryoFlg;
    public String trkDate;
    public String updDate;

    private Valve valve;

    public Kouji kouji;

    public Kiki kiki;

    public List<Buhin> buhin;

    public Koujirelation koujirelation;

    public String toText(){
        String text =
                        id+"\t,"+
                        koujiId+"\t,"+
                        koujirelationId+"\t,"+
                        kiki.kikiId+"\t,"+
                        tenkenDate+"\t,"+
                        tenkenNendo+"\t,"+
                        tenkenRank+"\t,"+
                        tenkennaiyo+"\t,"+
                        tenkenkekka+"\t,"+
                        tenkenBikou+"\t,"+
                        kanryoFlg+"\t,"+
                        trkDate+"\t,"+
                        updDate+"\t,"+"End"+"\t,"+"TRKJID"+koujiId+"End"+"\t,"+"TR"+id+"End"+"\t,"+"KR"+koujirelationId+"End"+"\t,"+"KI"+kiki.kikiId+"End";
        return text;
    }

    public TenkenRirekiUtil toTenkenRirekiUtil(String bodyText){
        TenkenRirekiUtil tenkenRirekiUtil=new TenkenRirekiUtil();
        String words[] = bodyText.split("\t,");

        tenkenRirekiUtil.setId(Integer.parseInt(words[0]));
        tenkenRirekiUtil.setKoujiId(Integer.parseInt(words[1]));
        tenkenRirekiUtil.setKoujirelationId(Integer.parseInt(words[2]));
        tenkenRirekiUtil.setTenkenDate(words[4]);
        tenkenRirekiUtil.setTenkenNendo(words[5]);
        tenkenRirekiUtil.setTenkenRank(words[6]);
        tenkenRirekiUtil.setTenkennaiyo(words[7]);
        tenkenRirekiUtil.setTenkenkekka(words[8]);
        tenkenRirekiUtil.setTenkenBikou(words[9]);
        tenkenRirekiUtil.setKanryoFlg(words[10]);
        tenkenRirekiUtil.setTrkDate(words[11]);
        tenkenRirekiUtil.setUpdDate(words[12]);

        return  tenkenRirekiUtil;

    }

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public int getKoujiId(){return koujiId;}
    public void setKoujiId(int koujiId){this.koujiId=koujiId;}

    public int getKoujirelationId(){return  koujirelationId;}
    public void setKoujirelationId(int koujirelationId){this.koujirelationId=koujirelationId;}

    public String getTenkenDate(){return tenkenDate;}
    public void setTenkenDate(String tenkenDate){this.tenkenDate=tenkenDate;}

    public String getTenkenNendo(){return tenkenNendo;}
    public void setTenkenNendo(String tenkenNendo){this.tenkenNendo=tenkenNendo;}

    public String getTenkenRank(){return  tenkenRank;}
    public void setTenkenRank(String tenkenRank){this.tenkenRank=tenkenRank;}

    public String getTenkennaiyo(){return  tenkennaiyo;}
    public void setTenkennaiyo(String tenkennaiyo){this.tenkennaiyo=tenkennaiyo;}

    public String getTenkenkekka(){return  tenkenkekka;}
    public void setTenkenkekka(String tenkenkekka){this.tenkenkekka=tenkenkekka;}

    public String getTenkenBikou() {return tenkenBikou;}
    public void setTenkenBikou(String tenkenBikou) {this.tenkenBikou = tenkenBikou;}

    public String getKanryoFlg(){return kanryoFlg;}
    public void setKanryoFlg(String kanryoFlg){this.kanryoFlg=kanryoFlg;}

    public String getTrkDate(){return  trkDate;}
    public void setTrkDate(String trkDate){this.trkDate=trkDate;}

    public String getUpdDate(){return  updDate;}
    public void setUpdDate(String updDate){this.updDate=updDate;}

    public Valve getValve() {
        return valve;
    }

    public void setValve(Valve valve) {
        this.valve = valve;
    }

    public Kouji getKouji(){return  kouji;}
    public void setKouji(Kouji kouji){this.kouji=kouji;}

    public Kiki getKiki(){return  kiki;}
    public void setKiki(Kiki kiki){this.kiki=kiki;}

    public List<Buhin> getBuhin(){return  buhin;}
    public void setBuhin(List<Buhin> buhin){this.buhin=buhin;}

    public Koujirelation getKoujirelation(){return  koujirelation;}
    public void setKoujirelation(Koujirelation koujirelation){this.koujirelation=koujirelation;}


}
