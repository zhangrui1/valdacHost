package com.toyo.vh.dao;

import com.toyo.vh.entity.Kenan;

import java.util.List;

/**
 * Created by zhangrui on 14/11/14.
 */
public interface KenanMapper {

    /**IDから懸案情報を取得*/
    public Kenan findKenanById(String id);

    /**全ての懸案情報を取得*/
    public List<Kenan> findAllKenan();

    /**KoujiIDから懸案情報を取得*/
    public List<Kenan> findKenanByKoujiId(String koujiId);

    /**relationIDから懸案情報を取得*/
    public Kenan findKenanBykoujirelationId(String koujirelationId);

    /**KikiIDから懸案情報を取得*/
    public List<Kenan> findKenanByKikiId(String kikiId);

    /**kikisysIdから全部懸案情報を取得*/
    public List<Kenan> findKenanByKikisysId(String kikisysId);

    /**kikisysIdから未対応懸案情報を取得*/
    public List<Kenan> findKenanByKikisysIdForAll(String kikisysId);

    /**新規懸案を追加する*/
    public void insertKenan(Kenan kenan);

    /**最新追加した懸案のIDを取得*/
    public int getLastInsertId();
    /**懸案情報を更新する*/
    public void updateKenan(Kenan kenan);
    /**懸案情報を削除する*/
    public void deleteKenan(Kenan kenan);
}
