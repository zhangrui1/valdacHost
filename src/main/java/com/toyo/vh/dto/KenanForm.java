package com.toyo.vh.dto;

import com.toyo.vh.entity.Kenan;
import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.Kouji;
import com.toyo.vh.entity.Valve;

/**
 * Created by Lsr on 12/4/14.
 * 懸案
 */
public class KenanForm extends Kenan {
    public Kouji kouji;
    public Valve valve;
    public Kiki kiki;

    public Kouji getKouji() {
        return kouji;
    }

    public void setKouji(Kouji kouji) {
        this.kouji = kouji;
    }

    public Valve getValve() {
        return valve;
    }

    public void setValve(Valve valve) {
        this.valve = valve;
    }

    public Kiki getKiki() {
        return kiki;
    }

    public void setKiki(Kiki kiki) {
        this.kiki = kiki;
    }

    public KenanForm copyKenanForm(Kenan kenan){
        KenanForm newKenanForm=new KenanForm();
        newKenanForm.setId(kenan.getId());
        newKenanForm.setKoujiId(kenan.getKoujiId());
        newKenanForm.setkikisysId(kenan.getKikisysId());
        newKenanForm.setKoujirelationId(kenan.getKoujirelationId());
        newKenanForm.setKikiId(kenan.getKikiId());
        newKenanForm.setHakkenDate(kenan.getHakkenDate());
        newKenanForm.setTaisakuDate(kenan.getTaisakuDate());
        newKenanForm.setTaiouFlg(kenan.getTaiouFlg());
        newKenanForm.setJisyo(kenan.getJisyo());
        newKenanForm.setBuhin(kenan.getBuhin());
        newKenanForm.setGensyo(kenan.getGensyo());
        newKenanForm.setYouin(kenan.getYouin());
        newKenanForm.setTaisaku(kenan.getTaisaku());
        newKenanForm.setHakkenJyokyo(kenan.getHakkenJyokyo());
        newKenanForm.setSyotiNaiyou(kenan.getSyotiNaiyou());
        newKenanForm.setTrkDate(kenan.getTrkDate());
        newKenanForm.setUpdDate(kenan.getUpdDate());

        return  newKenanForm;
    }
}
