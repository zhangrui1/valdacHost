package com.toyo.vh.dao;

import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.Koujirelation;
import com.toyo.vh.entity.Valve;

import java.util.List;

/**
 * Created by zhangrui on 14/11/14.
 */
public interface KoujirelationMapper {

    /**IDから関係データを取得*/
    public Koujirelation findKoujirelationById(String id);

    /**IDから工事、弁、機器の関連データを取得*/
    public List<Koujirelation> findAllKoujirelationByKoujiid(String id);

    /**弁IDから工事、弁、機器の関連データを取得*/
    public List<Koujirelation> findKoujirelationBykikiSystemId(String kikisysid);

    /**koujiidと弁IDから機器IDリストを取得*/
    public List<Koujirelation> findKoujirelationByKoujirelation(Koujirelation koujirelation);

    /**koujiidから関連の弁IDリストを取得*/
    public List<Valve> findKikisysListByKoujiid(String koujiid);

    /**koujiidと弁IDから機器IDリストを取得*/
    public List<Kiki> findKikiListByKoujiidAndKikisys(Koujirelation koujirelation);

    /**工事関係表に追加する*/
    public void insertKoujirelation(Koujirelation koujirelation);

    /**最新追加したデータのIDを取得*/
    public int getLastInsertId();

    /**データ削除する*/
    public void deleteKoujirelation(Koujirelation koujirelation);

    /**工事IDからデータ削除する*/
    public void deleteKoujirelationbyKoujiid(String koujiid);

    /**全ての関連データを取得*/
    public List<Koujirelation> findAllKoujirelation();

    public void deleteKoujirelationByKoujiidAndKikisysId(Koujirelation koujirelation);

    List<Integer> findkikiIdListByKoujiidAndKikisys(Koujirelation koujirelation);
}
