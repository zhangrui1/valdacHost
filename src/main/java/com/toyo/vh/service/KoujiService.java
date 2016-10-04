package com.toyo.vh.service;

import com.toyo.vh.dao.KoujiMapper;
import com.toyo.vh.entity.Kouji;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangrui on 14/11/14.
 */
@Service
public class KoujiService {

    @Resource
    KoujiMapper koujiMapper;
    /**工事IDから工事情報を取得*/
    public Kouji getKoujiById(String id){

        Kouji kouji=koujiMapper.findKoujiById(id);
        return  kouji;
    }

    /**該ユーザが担当している工事リストを取得*/
    public List<Kouji> getKoujiByPerson(String username){

        List<Kouji> kouji=koujiMapper.findKoujiByperson(username);
        return  kouji;
    }

    /**該ユーザが担当している工事リスト+工事状態　取得*/
    public List<Kouji> getKoujiByPersonAndStatus(String username,String status){
        Kouji koujiTemp=new Kouji();
        koujiTemp.setPerson(username);
        koujiTemp.setStatus(status);
        List<Kouji> kouji=koujiMapper.findByPersonAndKoujiStatus(koujiTemp);
        return  kouji;
    }


    /**該ユーザが担当している最新の工事リストのトップ１０を取得*/
    public List<Kouji> getKoujiByPersonAndKjNo(Kouji searchKouji){
        List<Kouji> koujis=koujiMapper.findKoujiByPersonAndKjNo(searchKouji);
        return  koujis;
    }

    /**該ユーザが担当している最新の工事リストのトップ１０を取得*/
    public List<Kouji> getLastedTenKoujiByPerson(String username){

        List<Kouji> kouji=koujiMapper.findLastedTenKoujiByperson(username);
        return  kouji;
    }

    /**該ユーザが担当している最新の工事リストのトップ１０＋工事状態　を取得*/
    public List<Kouji> getLastedTenKoujiByPersonAndKoujiStatus(String username,String status){
        Kouji koujiTemp=new Kouji();
        koujiTemp.setPerson(username);
        koujiTemp.setStatus(status);
        List<Kouji> kouji=koujiMapper.findLastedTenKoujiBypersonAndKoujiStatus(koujiTemp);
        return  kouji;
    }

    /**該ユーザが最近更新した工事リストのトップ１０を取得*/
    public List<Kouji> getUpdateTenKoujiByPerson(String username){

        List<Kouji> kouji=koujiMapper.findUpdateTenKoujiByperson(username);
        return  kouji;
    }

    /**該ユーザが最近更新した工事リストのトップ１０+工事状態　を取得*/
    public List<Kouji> getUpdateTenKoujiByPersonAndKoujiStatus(String username,String status){
        Kouji koujiTemp=new Kouji();
        koujiTemp.setPerson(username);
        koujiTemp.setStatus(status);
        List<Kouji> kouji=koujiMapper.findUpdateTenKoujiBypersonAndKoujiStatus(koujiTemp);
        return  kouji;
    }

    /**新規工事をkoujiテーブルに追加する*/
    public Kouji addKouji(Kouji kouji){
        //append Date
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        kouji.setTrkDate(sdf1.format(date));
        kouji.setUpdDate(sdf1.format(date));

        kouji.setDelFlgkouji("0");
        koujiMapper.insertKouji(kouji);
        //make id
        kouji.setId(koujiMapper.getLastInsertId());
        return kouji;
    }

    public Kouji updateKouji(Kouji kouji){
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        kouji.setUpdDate(sdf1.format(date));

        koujiMapper.updateKouji(kouji);
        return kouji;
    }

    public void deleteKouji(Kouji kouji){
        koujiMapper.deleteKouji(kouji);
    }
}
