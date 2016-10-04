package com.toyo.vh.dao;

import com.toyo.vh.entity.TenkenRireki;
import com.toyo.vh.entity.TenkenRirekiUtil;

import java.util.List;

/**
 * Created by zhangrui on 14/11/14.
 */
public interface TenkenRirekiMapper {

    /**IDから点検機器情報を取得*/
    public TenkenRireki findTenkenRirekiById(String id);

    /**tenkenRirekiから点検機器情報を取得 存在するかどうかチェック用*/
    public List<TenkenRireki> findTenkenRirekiByTenkenRireki(TenkenRireki tenkenRireki);

    /**KoujiIDから点検機器情報を取得*/
    public List<TenkenRirekiUtil> findTenkenRirekiByKoujiId(String koujiId);

    /**kikiIdから点検機器情報を取得*/
    public List<TenkenRirekiUtil> findTenkenRirekiByKikiId(String kikiId);

    /**KoujiIDから点検機器情報を取得*/
    public List<TenkenRireki> findTenkenRirekiByKoujiIdToTenkenRireki(String koujiId);

    /**KoujiIDから点検機器情報を取得*/
    public TenkenRirekiUtil findTenkenRirekiByKoujirelationId(String koujirelationId);

    /**KoujiID,statusから点検機器情報を取得*/
    public List<TenkenRirekiUtil> findTenkenRirekiByKoujiIdAndKanryoFlg(TenkenRireki tenkenRireki);

    /**新規点検機器を追加する*/
    public void insertTenkenRireki(TenkenRireki tenkenRireki);

    /**最新追加した工事のIDを取得*/
    public int getLastInsertId();

    /**工事情報を更新する*/
    public void updateTenkenRireki(TenkenRireki tenkenRireki);

    /**工事IDにより、点検ランク一括を更新する*/
    public void updateTenkenRirekiAllRankByKoujiID(TenkenRireki tenkenRireki);

    /**工事情報を削除する*/
    public void deleteTenkenRireki(TenkenRireki tenkenRireki);

    /**工事IDkら情報を削除する*/
    public void deleteTenkenRirekiByKoujiId(String koujiId);

    /**全て点検機器情報を取得*/
    public List<TenkenRireki> findAllTenkenRireki();
}
