package com.toyo.vh.entity;

/**
 * Created by zhangrui on 14/11/18.
 * 点検機器エンティティ
 */
public class TenkenRireki {
    public int id;
    public int koujiId;
    public int koujirelationId;
    public int kikiId;
    public String tenkenDate;
    public String tenkenNendo;
    public String tenkenRank;
    public String tenkennaiyo;
    public String tenkenkekka;
    public String tenkenBikou;
    public String kanryoFlg;
    public String trkDate;
    public String updDate;

    //lucene初期化時使う
    public String toText(){
        String text =
                id+"\t,"+
                        koujiId+"\t,"+
                        koujirelationId+"\t,"+
                        kikiId+"\t,"+
                        tenkenDate+"\t,"+
                        tenkenNendo+"\t,"+
                        tenkenRank+"\t,"+
                        tenkennaiyo+"\t,"+
                        tenkenkekka+"\t,"+
                        tenkenBikou+"\t,"+
                        kanryoFlg+"\t,"+
                        trkDate+"\t,"+
                        updDate+"\t,"+"End"+"\t,"+"TRKJID"+koujiId+"End"+"\t,"+"TR"+id+"End"+"\t,"+"KR"+koujirelationId+"End"+"\t,"+"KI"+kikiId+"End";
        return text;
    }

    //luceneからValveに戻る時使う
    public TenkenRireki toTenkenRireki(String bodyText){
        TenkenRireki tenkenRireki=new TenkenRireki();
        String words[] = bodyText.split("\t,");

        tenkenRireki.setId(Integer.parseInt(words[0]));
        tenkenRireki.setKoujiId(Integer.parseInt(words[1]));
        tenkenRireki.setKoujirelationId(Integer.parseInt(words[2]));
        tenkenRireki.setKikiId(Integer.parseInt(words[3]));
        tenkenRireki.setTenkenDate(words[4]);
        tenkenRireki.setTenkenNendo(words[5]);
        tenkenRireki.setTenkenRank(words[6]);
        tenkenRireki.setTenkennaiyo(words[7]);
        tenkenRireki.setTenkenkekka(words[8]);
        tenkenRireki.setTenkenBikou(words[9]);
        tenkenRireki.setKanryoFlg(words[10]);
        tenkenRireki.setTrkDate(words[11]);
        tenkenRireki.setUpdDate(words[12]);

        return  tenkenRireki;

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

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public int getKoujiId(){return koujiId;}
    public void setKoujiId(int koujiId){this.koujiId=koujiId;}

    public int getKoujirelationId(){return  koujirelationId;}
    public void setKoujirelationId(int koujirelationId){this.koujirelationId=koujirelationId;}

    public int getKikiId(){return kikiId;}
    public void setKikiId(int kikiId){this.kikiId=kikiId;}

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
}
