package com.toyo.vh.entity;

/**
 * Created by Lsr on 10/27/14.
 * 工事関係表エンティティ
 */
public class Koujirelation {

    public int id;
    public int koujiid;
    public int kikisysid;
    public int kikiid;

    public String toText(){
        String text=
                id+"\t,"+
                koujiid+"\t,"+
                kikisysid+"\t,"+
                kikiid+"\t,"+"End"+"\t,"+"KR"+id+"End"+"\t,"+"TRKJID"+koujiid+"End"+"\t,"+"VA"+kikisysid+"End";

        return text;
    }
    public Koujirelation toKoujirelation(String bodyText){
        Koujirelation koujirelation=new Koujirelation();

        String words[] = bodyText.split("\t,");
        koujirelation.setId(Integer.parseInt(words[0]));
        koujirelation.setKoujiid(Integer.parseInt(words[1]));
        koujirelation.setKikisysid(Integer.parseInt(words[2]));
        koujirelation.setKikiid(Integer.parseInt(words[3]));
        return koujirelation;
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

    public int getKoujiid() {
        return koujiid;
    }

    public void setKoujiid(int koujiid) {
        this.koujiid = koujiid;
    }
}
