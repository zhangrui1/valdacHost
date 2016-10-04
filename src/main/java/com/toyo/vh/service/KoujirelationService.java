package com.toyo.vh.service;

import com.toyo.vh.dao.KoujirelationMapper;
import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.Koujirelation;
import com.toyo.vh.entity.Valve;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangrui on 14/11/14.
 */
@Service
public class KoujirelationService {

    @Resource
    KoujirelationMapper koujirelationMapper;

    /**新規追加する*/
    public Koujirelation getKoujirelationById(String id){
        Koujirelation koujirelation=koujirelationMapper.findKoujirelationById(id);
        return koujirelation;
    }

    /**工事ID,弁ID,機器IDにより、工事関係をする*/
    public List<Koujirelation> getKoujirelationByKoujirelation(Koujirelation koujirelation){
        List<Koujirelation> Temkoujirelation=koujirelationMapper.findKoujirelationByKoujirelation(koujirelation);
        return Temkoujirelation;
    }

    /**工事関連データを所得する*/
    public List<Koujirelation> getAllKoujirelationByKoujiid(String id){
        List<Koujirelation> koujirelations=koujirelationMapper.findAllKoujirelationByKoujiid(id);
        return koujirelations;
    }

    /**弁IDから関連データを所得する*/
    public List<Koujirelation> getKoujirelationBySystemId(String id){
        List<Koujirelation> koujirelations=koujirelationMapper.findKoujirelationBykikiSystemId(id);
        return koujirelations;
    }

    /**工事IDから弁リストを所得する*/
    public List<Valve> getKikisysListByKoujiid(String koujiid){
         List<Valve> valves=koujirelationMapper.findKikisysListByKoujiid(koujiid);
        return valves;
    }

    /**工事IDと弁IDから　機器リストを所得する*/
    public List<Kiki> getKikiListByKoujiidAndKikisys(String koujiid,String kikisysid){
        Koujirelation koujirelation=new Koujirelation();
        koujirelation.setKoujiid(Integer.valueOf(koujiid));
        koujirelation.setKikisysid(Integer.valueOf(kikisysid));
        List<Kiki> kikis=koujirelationMapper.findKikiListByKoujiidAndKikisys(koujirelation);

        return kikis;
    }

    /**工事関係を追加*/
    public Koujirelation addKoujirelation(Koujirelation koujirelation){
        koujirelationMapper.insertKoujirelation(koujirelation);
        koujirelation.setId(koujirelationMapper.getLastInsertId());
        return koujirelation;
    }

    /**工事関係IDにより、工事関係を削除*/
    public void deleteKoujirelation(Koujirelation koujirelation){
        koujirelationMapper.deleteKoujirelation(koujirelation);
    }

    /**工事IDにより、工事関係を削除*/
    public void deleteKoujirelationByKoujiid(String koujiid){
        koujirelationMapper.deleteKoujirelationbyKoujiid(koujiid);
    }

    public void deleteKoujirelationByKoujiidAndKikisysId(String id, String s) {
        Koujirelation koujirelation = new Koujirelation();
        koujirelation.setKoujiid(Integer.valueOf(id));
        koujirelation.setKikisysid(Integer.valueOf(s));
        koujirelationMapper.deleteKoujirelationByKoujiidAndKikisysId(koujirelation);
    }

    public List<Integer> getKikiIdListByKoujiidAndKikisys(String koujiId, String kikiSysId){
        Koujirelation koujirelation = new Koujirelation();
        koujirelation.setKikisysid(Integer.valueOf(kikiSysId));
        koujirelation.setKoujiid(Integer.valueOf(koujiId));
        return koujirelationMapper.findkikiIdListByKoujiidAndKikisys(koujirelation);
    }
}
