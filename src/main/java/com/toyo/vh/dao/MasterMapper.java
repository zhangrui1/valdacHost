package com.toyo.vh.dao;

import com.toyo.vh.entity.Master;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zhangrui on 2014/10/15.
 */
public interface MasterMapper {

    /**弁に関するマスターデータを取得
     * @return  masterテーブルのデータ
     * */

    @Select("select * from master where type=#{type} ORDER BY name")
     public List<Master> findMasterByType(String type);

//    /**マスターデータを追加する
//     * @param  master
//     **/
//    @Insert("insert into Master(Type,Code,Ryaku,Name) values(#{Type},#{Code},#{Ryaku},#{Name})")
//    public void insertMasterByType(Master master);
}
