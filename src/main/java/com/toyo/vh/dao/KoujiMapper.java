package com.toyo.vh.dao;

import com.toyo.vh.entity.Kouji;

import java.util.List;

/**
 * Created by zhangrui on 14/11/14.
 */
public interface KoujiMapper {

    /**該ユーザが担当している工事リストを取得*/
    public Kouji findKoujiById(String id);

    /**工事番号と担当者で検索*/
    public List<Kouji> findKoujiByPersonAndKjNo(Kouji kouji);

    /**該ユーザが担当している工事リストを取得*/
    public List<Kouji> findKoujiByperson(String username);

    /**ユーザIDと工事状態により、工事を取得*/
    public List<Kouji> findByPersonAndKoujiStatus(Kouji kouji);

    /**該ユーザが担当している最新の工事リストのトップ１０を取得*/
    public List<Kouji> findLastedTenKoujiByperson(String username);

    /**ユーザIDと工事状態により、最新の工事リストのトップ１０を取得*/
    public List<Kouji> findLastedTenKoujiBypersonAndKoujiStatus(Kouji kouji);

    /**該ユーザが最近更新した工事リストのトップ１０を取得*/
    public List<Kouji> findUpdateTenKoujiByperson(String username);

    /**ユーザIDと工事状態により、更新された工事リストのトップ１０を取得*/
    public List<Kouji> findUpdateTenKoujiBypersonAndKoujiStatus(Kouji kouji);

    /**新規工事をkoujiテーブルに追加する*/
    public void insertKouji(Kouji kouji);

    /**最新追加した工事のIDを取得*/
    public int getLastInsertId();

    /**工事情報を更新する*/
    public void updateKouji(Kouji kouji);

    /**工事情報を削除する*/
    public void deleteKouji(Kouji kouji);

    /**全部の工事を取得*/
    public List<Kouji> findAllKouji();

    /**全部の工事を取得　状態と開始日付でソート*/
    public List<Kouji> findAllKoujiSort();

    /**locationにより　工事を取得　状態と開始日付でソート*/
    public List<Kouji> findKoujiByLocation(String location);

    /**locationにより　工事の工事番号とlocationdeで検索*/
    public List<Kouji> findKoujiByLocationAndKjNo(Kouji kouji);
}
