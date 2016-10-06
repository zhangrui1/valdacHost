package com.toyo.vh.dao;

import com.toyo.vh.entity.Location;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Lsr on 10/14/14.
 */
public interface LocationMapper {

    @Select("select * from vh_location order by kCodeLKana,kCodeMKana,kCodeSKana")
    public List<Location> findAllLocation();

    /**する*/
    public List<Location> findALocationByUserDepartment(Location location);

    /**kCodeLを抽出する*/
    public List<String> findKCodeL();

    /**kCodeMを抽出する*/
    public List<String> findKCodeMByL(Location location);

    /**kCodeSを抽出する*/
    public List<String> findKCodeSByLM(Location location);

}
