package com.toyo.vh.entity;

/**
 * Created by zhangrui on 14/11/18.
 * 懸案エンティティ
 */
public class Kenan {
    public int id;
    public int koujiId;
    public int kikisysId;
    public int koujirelationId;
    public int kikiId;
    public String hakkenDate;
    public String taisakuDate;
    public String taiouFlg;
    public String jisyo;
    public String buhin;
    public String gensyo;
    public String youin;
    public String taisaku;
    public String hakkenJyokyo;
    public String syotiNaiyou;
    public String delFlgKenan;
    public String trkDate;
    public String updDate;

    //lucene初期化時使う
    public String toText(){
        String text =
                        id+"\t,"+
                        koujiId+"\t,"+
                        koujirelationId+"\t,"+
                        kikiId+"\t,"+
                        hakkenDate+"\t,"+
                        taisakuDate+"\t,"+
                        taiouFlg+"\t,"+
                        jisyo+"\t,"+
                        buhin+"\t,"+
                        gensyo+"\t,"+
                        youin+"\t,"+
                        taisaku+"\t,"+
                        hakkenJyokyo+"\t,"+
                        syotiNaiyou+"\t,"+
                        delFlgKenan+"\t,"+
                        trkDate+"\t,"+
                        updDate+"\t,"+
                        kikisysId+"\t,"+"KEN"+id+"End"+"\t,"+"KR"+koujirelationId+"End"+"\t,"+"KJ"+koujiId+"End"+"\t,"+"KI"+kikiId+"End"+"\t,"+"VA"+kikisysId+"End"+"\t,";
        return text;
    }
    //luceneからKenanに戻る時使う
    public Kenan toKenan(String bodyText){
        Kenan kenan=new Kenan();

        String words[] = bodyText.split("\t,");
        kenan.setId(Integer.parseInt(words[0]));
        kenan.setKoujiId(Integer.parseInt(words[1]));
        kenan.setKoujirelationId(Integer.parseInt(words[2]));
        kenan.setKikiId(Integer.parseInt(words[3]));
        kenan.setHakkenDate(words[4]);
        kenan.setTaisakuDate(words[5]);
        kenan.setTaiouFlg(words[6]);
        kenan.setJisyo(words[7]);
        kenan.setBuhin(words[8]);
        kenan.setGensyo(words[9]);
        kenan.setYouin(words[10]);
        kenan.setTaisaku(words[11]);
        kenan.setHakkenJyokyo(words[12]);
        kenan.setSyotiNaiyou(words[13]);
        kenan.setDelFlgKenan(words[14]);
        kenan.setTrkDate(words[15]);
        kenan.setUpdDate(words[16]);
        kenan.setkikisysId(Integer.parseInt(words[17]));
        return kenan;
    }

    public Kenan copyKenan(Kenan kenan){
        Kenan newKenan=new Kenan();
        newKenan.setId(kenan.getId());
        newKenan.setKoujiId(kenan.getKoujiId());
        newKenan.setkikisysId(kenan.getKikisysId());
        newKenan.setKoujirelationId(kenan.getKoujirelationId());
        newKenan.setKikiId(kenan.getKikiId());
        newKenan.setHakkenDate(kenan.getHakkenDate());
        newKenan.setTaisakuDate(kenan.getTaisakuDate());
        newKenan.setTaiouFlg(kenan.getTaiouFlg());
        newKenan.setJisyo(kenan.getJisyo());
        newKenan.setBuhin(kenan.getBuhin());
        newKenan.setGensyo(kenan.getGensyo());
        newKenan.setYouin(kenan.getYouin());
        newKenan.setTaisaku(kenan.getTaisaku());
        newKenan.setHakkenJyokyo(kenan.getHakkenJyokyo());
        newKenan.setSyotiNaiyou(kenan.getSyotiNaiyou());
        newKenan.setTrkDate(kenan.getTrkDate());
        newKenan.setUpdDate(kenan.getUpdDate());

        return  newKenan;
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

    public int getKikisysId(){return kikisysId;}
    public void setkikisysId(int kikisysId){this.kikisysId=kikisysId;}

    public int getKoujirelationId(){return  koujirelationId;}
    public void setKoujirelationId(int koujirelationId){this.koujirelationId=koujirelationId;}

    public int getKikiId(){return kikiId;}
    public void setKikiId(int kikiId){this.kikiId=kikiId;}

    public String getHakkenDate(){return hakkenDate;}
    public void setHakkenDate (String hakkenDate){this.hakkenDate=hakkenDate;}

    public String getTaisakuDate(){return taisakuDate;}
    public void setTaisakuDate(String taisakuDate){this.taisakuDate=taisakuDate;}

    public String getTaiouFlg(){return  taiouFlg;}
    public void setTaiouFlg(String taiouFlg){this.taiouFlg=taiouFlg;}

    public String getJisyo(){return  jisyo;}
    public void setJisyo(String jisyo){this.jisyo=jisyo;}

    public String getBuhin(){return  buhin;}
    public void setBuhin(String buhin){this.buhin=buhin;}

    public String getGensyo(){return gensyo;}
    public void setGensyo(String gensyo){this.gensyo=gensyo;}

    public String getYouin(){return  youin;}
    public void setYouin(String youin){this.youin=youin;}

    public String getTaisaku() { return taisaku;}
    public void setTaisaku(String taisaku){this.taisaku=taisaku;}

    public String getHakkenJyokyo(){return  hakkenJyokyo;}
    public void setHakkenJyokyo(String hakkenJyokyo){this.hakkenJyokyo=hakkenJyokyo;}

    public String getSyotiNaiyou(){return syotiNaiyou;}
    public void setSyotiNaiyou(String syotiNaiyou){this.syotiNaiyou=syotiNaiyou;}

    public String getDelFlgKenan(){return delFlgKenan;}
    public void setDelFlgKenan(String delFlgKenan){this.delFlgKenan=delFlgKenan;}

    public String getTrkDate(){return  trkDate;}
    public void setTrkDate(String trkDate){this.trkDate=trkDate;}

    public String getUpdDate(){return  updDate;}
    public void setUpdDate(String updDate){this.updDate=updDate;}
}
