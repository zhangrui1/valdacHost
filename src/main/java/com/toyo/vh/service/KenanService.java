package com.toyo.vh.service;

import com.toyo.vh.dao.KenanMapper;
import com.toyo.vh.entity.Kenan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangrui on 14/11/18.
 */
@Service
public class KenanService {

    @Resource
    KenanMapper kenanMapper;

    /**IDから懸案情報を取得*/
    public Kenan getKenanById(String id){
        Kenan kenan = kenanMapper.findKenanById(id);
        return kenan;
    }

    /**KoujiIDから懸案情報を取得*/
    public List<Kenan> getKenanByKoujiId(String koujiId){
        List<Kenan> kenans= kenanMapper.findKenanByKoujiId(koujiId);
        return  kenans;
    }

    /**koujirelationIDから懸案情報を取得*/
    public Kenan getKenanBykoujirelationId(String koujirelationId){
        Kenan kenan= kenanMapper.findKenanBykoujirelationId(koujirelationId);
        return  kenan;
    }

    /**KikisysIDから懸案情報を取得*/
    public List<Kenan> getKenanByKikisysId(String kikisysId){
        List<Kenan> kenans= kenanMapper.findKenanByKikisysId(kikisysId);
        return  kenans;
    }

    /**KikisysIDから未対応懸案情報を取得*/
    public List<Kenan> getKenanByKikisysIdForAll(String kikisysId){
        List<Kenan> kenans= kenanMapper.findKenanByKikisysIdForAll(kikisysId);
        return  kenans;
    }

    /**KikiIDから懸案情報を取得*/
    public List<Kenan> getKenanByKikiId(String kikiId){
        List<Kenan> kenans= kenanMapper.findKenanByKikiId(kikiId);
        return  kenans;
    }

    /**新規懸案*/
    public Kenan addKenan(Kenan kenan){
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        kenan.setUpdDate(sdf1.format(date));
        kenan.setTrkDate(sdf1.format(date));
        kenan.setDelFlgKenan("0");

        kenanMapper.insertKenan(kenan);
        kenan.setId(kenanMapper.getLastInsertId());
        return kenan;
    }

    /**懸案情報を更新する*/
    public Kenan updateKenan(Kenan kenan){
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        kenan.setUpdDate(sdf1.format(date));
        kenanMapper.updateKenan(kenan);
        return kenan;
    }

    /**懸案情報を削除する*/
    public void deleteKenan(Kenan kenan){

        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        kenan.setUpdDate(sdf1.format(date));

        kenanMapper.deleteKenan(kenan);
    }


}
