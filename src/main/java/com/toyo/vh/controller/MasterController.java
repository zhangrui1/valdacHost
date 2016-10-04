package com.toyo.vh.controller;

import com.google.gson.Gson;
import com.toyo.vh.entity.Master;
import com.toyo.vh.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Lsr on 11/19/14.
 */

@Controller
@RequestMapping("/master")
public class MasterController {

    @Autowired
    MasterService masterService;

    /**
     * typeにより、masterデータ取得
     *
     * @param type タイプ名
     *
     * @return String　masterデータ
     * */
    @RequestMapping(value="/getMasterByTypeJson",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getMasterJson(@RequestParam("type") String type) {

        List<Master> masterList = masterService.getMasterByType(type);
        //ryakuにより　ソート
        Collections.sort(masterList,
                new Comparator<Master>() {
                    @Override
                    public int compare(Master entry1,
                                       Master entry2) {
                        return (entry1.getRyaku())
                                .compareTo(entry2.getRyaku());
                    }
                });
        Gson gson = new Gson();
        return gson.toJson(masterList);
    }
}