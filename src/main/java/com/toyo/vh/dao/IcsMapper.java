package com.toyo.vh.dao;


import com.toyo.vh.entity.Ics;

import java.util.List;

/**
 * Created by zhangrui on 2015/2/5.
 */
public interface IcsMapper {

    /**全部のmasterデータを取得*/
     public List<Ics> findAllIcs();

    /**Idにより、マスターデータを取得*/
     public Ics findIcsById(String id);

    /**IcsNumにより、データを取得*/
    public Ics findIcsByIcsNum(String icsNum);

}
