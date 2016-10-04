package com.toyo.vh.entity;

/**
 * Created by zhangrui on 14/11/14.
 * 工事エンティティ
 */
public class Kouji {

    public int id;
    public String kjNo;
    public String kjMeisyo;
    public String kjKbn;
    public String bgnYmd;
    public String endYmd;
    public String nextYmd;
    public String nendo;
    public String syukan;
    public String gyosyaRyakuA;
    public String location;
    public String status;
    public String person;
    public String delFlgkouji;
    public String trkDate;
    public String updDate;

    //lucene初期化時使う
    public String toText(){
        String text =
                id+"\t,"+
                kjNo+"\t,"+
                kjMeisyo+"\t,"+
                kjKbn+"\t,"+
                bgnYmd+"\t,"+
                endYmd+"\t,"+
                nextYmd+"\t,"+
                nendo+"\t,"+
                syukan+"\t,"+
                gyosyaRyakuA+"\t,"+
                location+"\t,"+
                status+"\t,"+
                person+"\t,"+
                delFlgkouji+"\t,"+
                trkDate+"\t,"+
                updDate+"\t,"+"End"+"\t,"+"KJ"+id+"End";

        return text;
    }

    //luceneからkoujiに戻る時使う
    public Kouji toKouji(String bodyText){
        Kouji kouji=new Kouji();

        String words[] = bodyText.split("\t,");
        kouji.setId(Integer.parseInt(words[0]));
        kouji.setKjNo(words[1]);
        kouji.setKjMeisyo(words[2]);
        kouji.setKjKbn(words[3]);
        kouji.setBgnYmd(words[4]);
        kouji.setEndYmd(words[5]);
        kouji.setNextYmd(words[6]);
        kouji.setNendo(words[7]);
        kouji.setSyukan(words[8]);
        kouji.setGyosyaRyakuA(words[9]);
        kouji.setLocation(words[10]);
        kouji.setStatus(words[11]);
        kouji.setPerson(words[12]);
        kouji.setDelFlgkouji(words[13]);
        kouji.setTrkDate(words[14]);
        kouji.setUpdDate(words[15]);
        return kouji;
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

    public int getId(){return  id;}

    public void setId(int id){this.id=id;}

    public String getKjNo(){return  kjNo;}

    public void setKjNo(String kjNo){this.kjNo=kjNo;}

    public String getKjMeisyo(){return  kjMeisyo;}

    public void setKjMeisyo(String kjMeisyo){this.kjMeisyo=kjMeisyo;}

    public String getKjKbn(){return  kjKbn;}

    public void setKjKbn(String kjkbn){this.kjKbn=kjkbn;}

    public String getBgnYmd(){return  bgnYmd;}

    public void setBgnYmd(String bgnYmd){this.bgnYmd=bgnYmd;}

    public String getEndYmd(){return  endYmd;}

    public void setEndYmd(String endYmd){this.endYmd=endYmd;}

    public String getNextYmd(){return  nextYmd;}

    public void setNextYmd(String nextYmd){this.nextYmd=nextYmd;}

    public String getNendo(){return  nendo;}

    public void setNendo(String nendo){this.nendo=nendo;}

    public String getSyukan(){return  syukan;}

    public void setSyukan(String syukan){this.syukan=syukan;}

    public String getGyosyaRyakuA(){return gyosyaRyakuA;}

    public void setGyosyaRyakuA(String gyosyaRyakuA){this.gyosyaRyakuA=gyosyaRyakuA;}

    public String getLocation(){return location;}

    public void setLocation(String location){this.location=location;}

    public String getStatus(){return status;}

    public void setStatus(String status){this.status=status;}

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDelFlgkouji(){return delFlgkouji;}

    public void setDelFlgkouji(String delFlgkouji){this.delFlgkouji=delFlgkouji;}

    public String getTrkDate(){return trkDate;}

    public void setTrkDate(String trkDate){this.trkDate=trkDate;}

    public String getUpdDate(){return  updDate;}

    public void setUpdDate(String updDate){this.updDate=updDate;}

}
