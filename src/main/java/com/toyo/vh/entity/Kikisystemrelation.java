package com.toyo.vh.entity;

/**
 * Created by Lsr on 10/27/14.
 * 弁、機器と部品の関係のエンティティ
 */
public class Kikisystemrelation {

    public int id;
    public int kikisysid;
    public int kikiid;
    public int buhinid;

    //lucene初期化時使う
    public String toText(){
        String text =
                id+"\t,"+
                kikisysid+"\t,"+
                kikiid+"\t,"+
                buhinid+"\t,"+
                "End"+"\t,"+
                "VAKIRE"+id+"End"+"\t,"+
                "VA"+kikisysid+"End"+"\t,"+
                "KI"+kikiid+"End"+"\t,"+
                "BU"+buhinid+"End";
        return text;
    }

    //luceneからKikisystemrelationに戻る時使う
    public Kikisystemrelation toKikisystemrelation(String bodyText){
        Kikisystemrelation kikisystemrelation=new Kikisystemrelation();

        String words[] = bodyText.split("\t,");
        kikisystemrelation.setId(Integer.parseInt(words[0]));
        kikisystemrelation.setKikisysid(Integer.parseInt(words[1]));
        kikisystemrelation.setKikiid(Integer.parseInt(words[2]));
        kikisystemrelation.setBuhinid(Integer.parseInt(words[3]));

        return kikisystemrelation;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKikisysid() {
        return kikisysid;
    }

    public void setKikisysid(int kikisysid) {
        this.kikisysid = kikisysid;
    }

    public int getKikiid() {
        return kikiid;
    }

    public void setKikiid(int kikiid) {
        this.kikiid = kikiid;
    }

    public int getBuhinid() {
        return buhinid;
    }

    public void setBuhinid(int buhinid) {
        this.buhinid = buhinid;
    }
}
