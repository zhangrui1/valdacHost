package com.toyo.vh.dao;

import com.toyo.vh.dto.ValveKikiForm;
import com.toyo.vh.entity.*;

import java.util.List;

/**
 * Created by zhangrui on 14/11/14.
 */
public interface ItemMapper {

    /**弁情報により、弁リストを取得*/
    public List<Valve> findByValve(Valve valve);

    /**機器情報により、機器リストを取得*/
    public List<Kiki> findByKiki(Kiki kiki);

    /**部品情報により、部品リストを取得*/
    public List<Buhin> findByBuhin(Buhin buhin);

    /**弁場所により、弁リストを取得*/
    public List<Valve> findByLocationName(Valve valve);

    /**工事IDにより、弁リストを取得*/
    public List<Valve> findKikisysByKoujiId(String koujiid);

    /**弁番号と弁場所により、弁リストを取得*/
    public List<Valve> findByVNo(Valve valve);

    /**弁番号により、弁リストを取得*/
    public List<Valve> findKikisysByVNo(Valve valve);

    /**弁IDと機器主管係により、機器リストを取得*/
    public List<Kiki> findKikiByKikisysIdSyukan(ValveKikiForm valveKikiForm);

    /**弁IDにより、機器リストを取得*/
    public List<Kiki> findKikiByKikisysId(String kikiSysId);

    /**弁IDにより、弁を取得*/
    public Valve findKikisysBykikisysId(String kikiSysId);

    /**機器IDにより、機器を取得*/
    public Kiki findKikiByKikiId(String kikiId);

    /**全部の弁を取得*/
    public List<Valve> findAllValve();

    /**全部の機器を取得*/
    public List<Kiki> findAllKiki();

    /**全部の部品を取得*/
    public List<Buhin> findAllBuhin();

    /**全部の弁、機器、部品関係データを取得*/
    public List<Kikisystemrelation> findAllKikisystemrelation();

    /**弁IDにより、懸案フラグを更新*/
    public void updateKenanFlgByKikisys(Valve valve);

}
