<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 10/20/14
  Time: 4:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../htmlframe/headFrame.jsp" />
<style type="text/css">
    <!--
    .src {
        /*overflow: scroll;   *//* スクロール表示 */
        overflow-x: auto;
        overflow-y: auto;
        width: 1168px;
        height: 500px;
    }
    /*スクロール用*/
    thead.scrollHead,tbody.scrollBody{
        /*display:block;*/
    }
    tbody.scrollBody{
        overflow-y:scroll;
        /*overflow-x: auto;*/
        /*width: 1168px;*/
        height:500px;
    }

</style>
<body class="skin-black">
<!-- header logo: style can be found in header.less -->
<c:import url="../htmlframe/headerFrame.jsp"/>
<div class="container">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <%--<h1>--%>
                <%--機器検索--%>
            <%--</h1>--%>
            <%--<ol class="breadcrumb">--%>
                <%--<li><i class="fa fa-dashboard"></i> Index</li>--%>
            <%--</ol>--%>
        </section><hr/>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- collection -->
                <div class="col-md-4">
                    <div class="btn-group" role="group">
                        <a href="/valdacHost/list/valveMult" class="btn btn-default" onclick="return saveSelectedForKiki()">複合検索</a>
                        <a href="/valdacHost/list/valve" class="btn btn-default" onclick="return saveSelectedForKiki()">弁検索</a>
                        <a href="/valdacHost/list/kikiSearch" class="btn btn-default bg-yellow active" onclick="return saveSelectedForKiki()">機器検索</a>
                        <a href="/valdacHost/list/buhinSearch" class="btn btn-default" onclick="return saveSelectedForKiki()">部品検索</a>
                    </div>
                </div>
            </div></br></br>
            <div class="row">
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-md-10">
                                <h4><i class="fa fa-download"></i> ファイルダウンロード </h4>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-2">
                                <form name="kikisysDL" action="/valdacHost/print/printKikisys"  method="post" onsubmit="checkKikisysIDListNull();return false;">
                                    <input type="hidden" name="idList" id="idList" value="" />
                                    <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="部品リスト印刷"/>
                                </form>
                            </div>
                            <div class="col-md-2">
                                <form name="kenan" action="/valdacHost/print/printKenanForKikisysList"  method="post" onsubmit="checkKenan();return false;">
                                    <input type="hidden" name="idList" id="idListKenan" value="" />
                                    <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="懸案リスト印刷"/><br>
                                    <input type="hidden" name="kenanFlgDL" id="kenanFlgDL" value="" />
                                    <input type="radio" name="valve-kenan-radio" class="valve-kenan-radio" value="true" checked> 全部
                                    <input type="radio" name="valve-kenan-radio" class="valve-kenan-radio" value="false"> 未対応のみ
                                </form>
                            </div>
                            <div class="col-md-2">
                                <form  name="GPListDL" action="/valdacHost/print/printGPForKikisysList"  method="post" onsubmit="checkGPListNull();return false;">
                                    <input type="hidden" name="idList" id="idListGP" value="" />
                                    <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="GP仕様印刷"/>
                                </form>
                            </div>
                            <div class="col-md-2">
                                <form  name="DaityoListDL" action="/valdacHost/print/printDaityoForKikisysList"  method="post" onsubmit="checkDaityoListNull();return false;">
                                    <input type="hidden" name="idList" id="idListDaityo" value="" />
                                    <input type="hidden" name="companyLocation" id="companyLocation" value="" />
                                    <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="台帳履歴印刷"/>
                                </form>
                            </div>
                            <div class="col-md-2">
                                <form  name="TokuBetuKakoKirokuListDL" action="/valdacHost/print/printTokuBetuKakoKirokuForKikisysList"  method="post" onsubmit="checkTokuBetuKakoKirokuListNull();return false;">
                                    <input type="hidden" name="idListTokuBetuKako" id="idListTokuBetuKako" value="" />
                                    <input type="hidden" name="koujiId" id="koujiId" value="" />
                                    <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="特別加工記録印刷"/>
                                </form>
                            </div>
                            <div class="col-md-2">
                                <form  name="SijisyoListDL" action="/valdacHost/print/printSijisyoForKikisysList"  method="post" onsubmit="checkSijisyoListNull();return false;">
                                    <input type="hidden" name="idListSijisyo" id="idListSijisyo" value="" />
                                    <input type="hidden" name="koujiIdSijisyo" id="koujiIdSijisyo" value="" />
                                    <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="指示書印刷"/>
                                </form>
                            </div>
                            </div><br>
                        <div class="row">
                            <div class="col-md-2">
                                <div class="input-group">
                                    <input type="text" name="table_search" id="table_search" class="form-control input-sm pull-right" style="width: 150px;" placeholder="Filter" value="${filterValveKiki}">
                                    <div class="input-group-btn">
                                        <button class="btn btn-sm btn-default" id="table_search_btn" onclick="filterPage();"><i class="fa fa-search"></i></button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="btn-group">
                                    <div class="input-group">
                                        <button class="btn btn-sm btn-default selected_search_btn" id="selected_search_btn" onclick="selectedValveList()">選択された弁のみ表示</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="btn-group">
                                    <div class="input-group">
                                        <button class="btn btn-sm btn-default" id="all_search_btn" onclick="allValveList()">全部弁表示</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div></br>
            <%--<div class="row">--%>
                <%--<div class="panel panel-success">--%>
                    <%--<div class="panel-heading">--%>
                        <%--<div class="row">--%>
                            <%--<div class="col-md-10">--%>
                                <%--<h4><i class="fa fa-download"></i> 機器検索 </h4>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="panel-body">--%>
                        <%--<div class="row">--%>
                            <%--<div class="col-md-5 col-xs-5">--%>
                                <%--<div class="input-group">--%>
                                    <%--<select id="currentLocation">--%>
                                        <%--<option>全部会社名</option>--%>
                                        <%--<c:forEach items="${nameList}" var="name">--%>
                                            <%--<option>${name}</option>--%>
                                        <%--</c:forEach>--%>
                                    <%--</select>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="col-md-7 col-xs-7">--%>
                                <%--<div class="input-group">--%>
                                    <%--valveはluncenの文字列の番です、luncen修正する際、ここのvalueも修正する必要--%>
                                    <%--<input type="radio" class="kiki-radio" name="kiki-radio" value="3">機器番号--%>
                                    <%--<input type="radio" class="kiki-radio" name="kiki-radio" value="4">機器名称--%>
                                    <%--<input type="radio" class="kiki-radio" name="kiki-radio" value="6">メーカ略--%>
                                    <%--<input type="radio" class="kiki-radio" name="kiki-radio" value="7">メーカ--%>
                                    <%--<input type="radio" class="kiki-radio" name="kiki-radio" value="8">型式番号--%>
                                    <%--<input type="radio" class="kiki-radio" name="kiki-radio" value="9">シリアル番号--%>
                                    <%--<input type="radio" class="kiki-radio" name="kiki-radio" value="10">オーダー番号--%>
                                    <%--<input type="radio" class="kiki-radio" name="kiki-radio" value="11">備考--%>
                                    <%--<input type="radio" class="kiki-radio" name="kiki-radio" value="">全体--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div></br></br>--%>
                        <%--<div class="row">--%>
                            <%--<div class="col-md-4 col-xs-4">--%>
                                <%--<form action="/valdacHost/item/kikiSearch" method="post" class="sidebar-form"  name="kikisearchForm">--%>
                                    <%--<div class="input-group">--%>
                                        <%--<input type="text" name="keyword" id="keyword-input" class="form-control" placeholder="Search..." value="${keywordMessage}" onkeypress="return check(event.keyCode)">--%>
                                            <%--<span class="input-group-btn">--%>
                                                <%--<button type='button'  id='keyword-btn' class="btn btn-flat btn-default" onclick="check(13);"><i class="fa fa-search"></i></button>--%>
                                            <%--</span>--%>
                                        <%--<input type="hidden" name="locationNameSelect" id="locationNameSelect" value="" />--%>
                                        <%--<input type="hidden" name="kikiRadioSelect" id="kikiRadioSelect" value="" />--%>
                                    <%--</div>--%>
                                <%--</form>--%>
                            <%--</div>--%>

                            <%--<div class="col-md-4 col-xs-4">--%>
                                <%--<div class="input-group">--%>
                                    <%--<input type="text" name="table_search" id="table_search" class="form-control input-sm pull-right" style="width: 150px;" placeholder="Filter" value="${filterValveKiki}">--%>
                                    <%--<div class="input-group-btn">--%>
                                        <%--<button class="btn btn-sm btn-default" id="table_search_btn" onclick="filterPage();"><i class="fa fa-search"></i></button>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div></br>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div></br>--%>

            <div class="row">
                <div class="panel panel-success">
                    <div class="panel-heading" id="searchForKikiHeader">
                        <div class="row">
                            <div class="col-md-10">
                                <h4><i class="glyphicon glyphicon-chevron-up" id="iClass"></i> 機器複数条件検索 </h4>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body" id="searchForKiki" >
                        <div class="row">
                            <div class="col-md-14">
                                <form action="/valdacHost/item/kikiSearchForSeikak" method="post" class="sidebar-form"  name="kikisearchFormForSeikak" onsubmit="return checkValveSearchForSeikak();">
                                    <div class="box-body">
                                        <div class="row">
                                            <div class="col-md-1">
                                                <span STYLE="color: red;">設置プラント(必須)</span>
                                            </div>
                                            <div class="col-md-5">
                                                <%--<input type="text" id="bikou" name="bikou" class="form-control" value="${kikiSearchForSeikak.bikou}" />--%>
                                                <div class="input-group">
                                                    <select id="bikou" name="bikou" class="form-control" value="${kikiSearchForSeikak.bikou}">
                                                        <option>全部会社名</option>
                                                        <c:forEach items="${nameList}" var="name">
                                                            <option>${name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-md-1">
                                                機器分類
                                            </div>
                                            <div class="col-md-2">
                                                <%--<input type="text" id="kikiBunrui" name="kikiBunrui" class="form-control" value="${kikiSearchForSeikak.kikiBunrui}" />--%>
                                                <select id="kikiBunrui" name="kikiBunrui" class="form-control" value="">
                                                    <option  <c:if test="${(kikiSearchForSeikak.kikiBunrui eq '')}">selected</c:if>></option>
                                                    <option  <c:if test="${(kikiSearchForSeikak.kikiBunrui eq '弁本体')}">selected</c:if>>弁本体</option>
                                                    <option  <c:if test="${(kikiSearchForSeikak.kikiBunrui eq '駆動部')}">selected</c:if>>駆動部</option>
                                                    <option  <c:if test="${(kikiSearchForSeikak.kikiBunrui eq '付属部')}">selected</c:if>>付属部</option>
                                                    <option  <c:if test="${(kikiSearchForSeikak.kikiBunrui eq '補助部')}">selected</c:if>>補助部</option>
                                                </select>
                                            </div>
                                        </div><br>
                                        <div class="row">
                                            <div class="col-md-1">
                                                機器番号
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="kikiNo" name="kikiNo" class="form-control" value="${kikiSearchForSeikak.kikiNo}" />
                                            </div>
                                            <div class="col-md-1">
                                                機器名称
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="kikiMei" name="kikiMei" class="form-control kikiMei" value="${kikiSearchForSeikak.kikiMei}" />
                                            </div>
                                            <div class="col-md-1">
                                                主管係
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="syukan" name="syukan" class="form-control syukan" value="${kikiSearchForSeikak.syukan}" />
                                            </div>
                                            <div class="col-md-1">
                                                型式番号
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="katasikiNo" name="katasikiNo" class="form-control" value="${kikiSearchForSeikak.katasikiNo}" />
                                            </div>
                                        </div><br>
                                        <div class="row">
                                            <div class="col-md-1">
                                                シリアル番号
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="serialNo" name="serialNo" class="form-control" value="${kikiSearchForSeikak.serialNo}" />
                                            </div>
                                            <div class="col-md-1">
                                                オーダー番号
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="orderNo" name="orderNo" class="form-control" value="${kikiSearchForSeikak.orderNo}" />
                                            </div>
                                            <div class="col-md-1">
                                                メーカー
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="makerRyaku" name="makerRyaku" class="form-control maker" placeholder="略称" value="${kikiSearchForSeikak.makerRyaku}" />
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="maker" name="maker" class="form-control maker" placeholder="メーカー" value="${kikiSearchForSeikak.maker}" />
                                            </div>
                                        </div><br>

                                        <div class="box-body clearfix">
                                            <div class="form-group">
                                                <div class="col-md-6">
                                                </div>
                                                <div class="col-md-1">
                                                </div>
                                                <div class="col-md-2">
                                                    <span id="valve_num" class="valve_num pull-right" STYLE="font-size: 16px;">ヒット弁 ${kikiSearchitemNum} 台</span>
                                                </div>
                                                <div class="col-md-2">
                                                    <button class="btn btn-danger">
                                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-search"></i> この条件で検索&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    </button>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-10">
                                                        <div id="loading" align="center"><img src="/valdacHost/img/loading.gif"></div>
                                                    </div>
                                                </div></br>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div></br>
                    </div>
                </div>
            </div></br>

             <div class="row">
                    <div class="box src">
                        <div class="box-body no-padding ">
                            <div class="list-group kiki-list result-list">
                                <table class="table table-hover select-table result-table table-bordered">
                                    <thead>
                                    <tr>
                                        <th nowrap><input type="checkbox" class="headCheckbox" onclick="selectAllItem()">(全選)</th>
                                        <th nowrap>詳細</th>
                                        <th nowrap>懸案</th>
                                        <th nowrap>弁番号</th>
                                        <th nowrap>弁設置プランド</th>
                                        <th nowrap>弁名称</th>
                                        <th nowrap>機器分類</th>
                                        <th nowrap>機器番号</th>
                                        <th nowrap>機器名称</th>
                                        <th nowrap>主関係</th>
                                        <th nowrap>メーカ略</th>
                                        <th nowrap>メーカー</th>
                                        <th nowrap>型式番号</th>
                                        <th nowrap>シリアル番号</th>
                                        <th nowrap>オーダー番号</th>
                                        <th nowrap>更新日付</th>
                                        <th nowrap>詳細</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${kikiSearchitemResults}" var="item">
                                        <tr class="valve-item" id="${item.valve.kikiSysId}">
                                            <td onclick="setSelectedColor(this)"><input type="checkbox" class="checkbox"  value="${item.valve.kikiSysId}"></td>
                                            <td>
                                                <a class="btn btn-primary btn-sm operation-button-btn" href="/valdacHost/item/${item.valve.kikiSysId}/${kikiOrBenFlg}/valve" onclick="return saveSelectedForKiki()">詳細</a>
                                            </td>
                                            <td nowrap class="data-td"><c:if test="${(item.valve.kenanFlg eq '1')}">〇</c:if></td>
                                            <td nowrap class="data-td">${item.valve.vNo}</td>
                                            <td nowrap class="data-td">${item.valve.locationName}</td>
                                            <td nowrap class="data-td">${item.valve.benMeisyo}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).kikiBunrui}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).kikiNo}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).kikiMei}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).syukan}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).makerRyaku}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).maker}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).katasikiNo}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).serialNo}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).orderNo}</td>
                                            <td nowrap class="data-td">${item.kikiList.get(0).updDate}</td>
                                            <td>
                                                <a class="btn btn-primary btn-sm operation-button-btn" href="/valdacHost/item/${item.valve.kikiSysId}/${kikiOrBenFlg}/valve" onclick="return saveSelectedForKiki()">詳細</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
        </section><!-- /.content -->
</div><!-- ./wrapper -->
<!-- add new calendar event modal -->

<script type="text/javascript">
    $(document).ready(function(){
        //filter関数
        filter("${filterValveKiki}");

        //選択されたKikiを設定する
        setSelectedForKiki();

        //弁精確検索
//        $("div#searchForKikiHeader").click(function(){
//            var tmp= document.getElementById("searchForKiki").style.display;
//            if(tmp=="block"){
//                //非表示場合
//                document.getElementById("searchForKiki").style.display="none";
//                document.getElementById("iClass").className="glyphicon glyphicon-chevron-down";
//            }else{
//                //表示場合
//                document.getElementById("searchForKiki").style.display="block";
//                document.getElementById("iClass").className="glyphicon glyphicon-chevron-up";
//            }
//        } );

        $("#keyword-input").focus(function(){
                $("#keyword-btn").toggleClass("bg-red");

        });
        $("#keyword-input").blur(function(){
                $("#keyword-btn").toggleClass("bg-red");
        });

        $(".result-table tr").mouseover(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","1");
        });
        $(".result-table tr").mouseout(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","0");
        });


//        //弁選択　行のどこをクリックしても選択できるよう
//        $('.valve-item').mouseup(function(obj){
//            var tr = obj.currentTarget;
//            var selected = $(tr).find('.checkbox');
//            var checkbox = selected[0];
//            if($(checkbox).prop("checked") == true){
//                $(checkbox).prop('checked', false);
//                $(tr).removeClass("bg-light-blue-gradient");
//            } else {
//                $(checkbox).prop('checked', true);
//                $(tr).addClass("bg-light-blue-gradient");
//
//            }
//        });

        window.onload = function(){
            $(function() {
                //ページの読み込みが完了したのでアニメーションはフェードアウトさせる
                $("#loading").fadeOut();
            });
        }
        // location selected に設定する設定
        var locationFirst="${kikiSearchForSeikak.bikou}";
        var objname=document.getElementById("bikou");
        checkSelect(objname,locationFirst);
    });

    //弁選択
    function setSelectedColor(obj){
        var td = obj.currentTarget;
        var tr=$(obj).parent();
        var selected = $(tr).find('.checkbox');
        var checkbox = selected[0];

        if($(checkbox).prop("checked") == true){
            $(checkbox).prop('checked', false);
            $(tr).removeClass("bg-light-blue-gradient");
        } else {
            $(checkbox).prop('checked', true);
            $(tr).addClass("bg-light-blue-gradient");

        }
    }

    //Filter
    function filterPage(){
        var keyword = $("#table_search").val();
        keyword = keyword.toLowerCase();
        keyword=keyword.trim();

        var trs = $(".result-table tbody tr");
        for(var i = 0;i<trs.length;i++){
            $(trs[i]).hide();
            var tds = $(trs[i]).find("td");

            for(var j = 1;j<(tds.length-1);j++){
                var tmpHtml = new String(tds[j].innerHTML);
                tmpHtml=tmpHtml.toLowerCase();
                if(tmpHtml.match(keyword)){
                    $(trs[i]).show();
                    break;
                }
            }
        }

        //sessionに保存する
        if(keyword.length<1){
            keyword=""
        }

        //sessionに保存
        $.get("/valdacHost/item/valveKikiFilter",{"filterKeyword":keyword},function(data){
            return true;
        });
    };
    //画面描画時　Filter
    function filter(val){
        var keyword=val;
        if(val=="keyword"){
            keyword="";
        }

        var trs = $(".result-table tbody tr");
        for(var i = 0;i<trs.length;i++){
            $(trs[i]).hide();
            var tds = $(trs[i]).find("td");

            for(var j = 1;j<(tds.length-1);j++){
                var tmpHtml = new String(tds[j].innerHTML);
                tmpHtml=tmpHtml.toLowerCase();
                if(tmpHtml.match(keyword)){
                    $(trs[i]).show();
                    break;
                }
            }
        }
    };

    //選択された弁リストを表示される　Filter
    function selectedValveList(){
        var keyword = $("#table_search").val();
        keyword = keyword.toLowerCase();
        keyword=keyword.trim();

        var trs = $(".select-table tbody tr");
        for(var i = 0;i<trs.length;i++){
            $(trs[i]).hide();
            var tds = $(trs[i]).find("td");
            //selected状態は表示されるように
            var selected = $(trs[i]).find('.checkbox');
            var checkbox = selected[0];
            if($(checkbox).prop("checked") == true){
                $(trs[i]).show();
            }
        }
    }

    //すべての弁を表示される　Filter
    function allValveList(){
        var trs = $(".select-table tbody tr");
        for(var i = 0;i<trs.length;i++){
            $(trs[i]).show();
        }
    }

    function checkSelect(obj,val){
        for(var i=0;i<obj.length;i++){
            if(obj[i].value==val){
                obj[i].selected=true;
                break;
            }
        }
    }

    function submitPrintForm(){
        //対象弁を記録
        var idList = "";
        $('.checkbox:checked').each(function(){
            idList = idList + $(this)[0].value + ',';
        });
        $('#idList').val(idList);
        $('#idListKenan').val(idList);
        $('#idListGP').val(idList);
        $('#idListDaityo').val(idList);
        $('#idListTokuBetuKako').val(idList);
        $('#idListSijisyo').val(idList);
    }

    function checkKenan(){
        //弁ラベル　懸案全部か？未対応のみか？
        $('.valve-kenan-radio:checked').each(function(){
            $('#kenanFlgDL').val($(this)[0].value);
        });

        var idList = $("#idListKenan").val();
//        var isKenan=false;
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            $.post("/valdacHost/print/checkKenanForKikisysList",{"idList":idList}, function(data){
                var isKenan = JSON.parse(data);
                if(isKenan) {
                    document.kenan.submit();
                }else{
                    alert("懸案はありません。");
                }
            });
        }
    }
    function  checkKikisysIDListNull() {
        var idList = $("#idList").val();
        console.log("idList="+idList);
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.kikisysDL.submit();
        }
    }

    function  checkGPListNull() {
        var idList = $("#idList").val();
        console.log("idList="+idList);
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.GPListDL.submit();
        }
    }
    //IdListは空場合はメッセージを表示する
    function  checkDaityoListNull() {
        var idList = $("#idListDaityo").val();
        console.log("idList="+idList);
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.DaityoListDL.submit();
        }
    }
    //IdListは空場合はメッセージを表示する
    function checkTokuBetuKakoKirokuListNull(){
        var idList = $("#idListTokuBetuKako").val();
        var idListArray=idList.split(",");
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else if(idListArray.length>11){
            alert("弁件数を最大１０個にしてください。");
        }else{
            document.TokuBetuKakoKirokuListDL.submit();
        }
    }
    //IdListは空場合はメッセージを表示する　この機能を破棄した
    function checkSijisyoListNull(){
        var idList = $("#idListSijisyo").val();
        var idListArray=idList.split(",");
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else if(idListArray.length>11){
            alert("弁件数を最大１０個にしてください。");
        }else{
            document.SijisyoListDL.submit();
        }
    }

    //検索キーワードにEnterキーを押す場合チェック
    function check(code){
        if(code==13){
            var keywords = new String($("#keyword-input").val());
            keywords = keywords.toLowerCase();
            if(keywords.length<1){
                alert("キーワードを入力してください");
                return false;
            } else if(keywords.charAt(keywords.length-1) == '-' || keywords.charAt(keywords.length-1) == '/') {
                alert("キーワードは正しくありません");
                return false;
            } else {
                var ills = new Array();
                ills = ['+', '&&', '||', '!', '(', ')' ,'{' ,'}', '[', ']', '^', '"', '~', '*', '?', ':'];
                for(var i = 0;i<ills.length;i++){
                    if(keywords.indexOf(ills[i]) > -1){
                        alert("キーワードは正しくありません");
                        return false;
                    }
                }

            }
            $("#keyword-input").val(keywords);
            //会社名を記録
            var location = $("#currentLocation").val();
            if(location == "全部会社名"){
                location = "";
            }
            $('#locationNameSelect').val(location);

            //kiki検索条件取得
            $('.kiki-radio:checked').each(function(){
                $('#kikiRadioSelect').val($(this)[0].value);
            });
            //処理待ち中にアニメーション表示する
            $("#loading").fadeIn();
            //sessionに保存
            $.get("/valdacHost/item/valveKikiFilter",{"filterKeyword":""},function(data){
                return true;
            });
            document.kikisearchForm.submit();
        }
    }
    function selectAllItem(){
        if(!$('.headCheckbox').prop('checked')){
            $('.select-table').find('.checkbox').each(function(){
                var checkbox = $(this)[0];
                if($(checkbox).prop('checked')== true) {
                    $(checkbox).prop('checked', false);
                }
            });
            $('.select-table').find('.valve-item').each(function(){
                var tr = $(this)[0];
                if($(tr).hasClass('bg-light-blue-gradient')){
                    $(tr).removeClass("bg-light-blue-gradient");
                }
            });
        } else {
            $('.select-table').find('.checkbox').each(function(){
                var checkbox = $(this)[0];
                if($(checkbox).prop('checked')== true) {
                }
                else {
                    $(checkbox).prop('checked', true);
                }
            });
            $('.select-table').find('.valve-item').each(function(){
                var tr = $(this)[0];
                if($(tr).hasClass('bg-light-blue-gradient')){
                }
                else {
                    $(tr).addClass("bg-light-blue-gradient");
                }
            });
        }
    }

    //検索キーワードに空チェック
    function checkValveSearchForSeikak(){
        var tmp="";
        tmp=tmp+$("#kikiBunrui").val();
        tmp=tmp+$("#kikiNo").val();
        tmp=tmp+$("#kikiMei").val();
        tmp=tmp+$("#syukan").val();
        tmp=tmp+$("#katasikiNo").val();
        tmp=tmp+$("#serialNo").val();
//        tmp=tmp+$("#bikou").val();
        tmp=tmp+$("#orderNo").val();
        tmp=tmp+$("#makerRyaku").val();
        tmp=tmp+$("#maker").val();


        if(($("#bikou").val())=="全部会社名"){
            alert("設置プラントにキーワードをご入力ください");
            return false;
        }else{
            return true;
        }
//        console.log("tmp="+tmp);
//        if(tmp.length<1){
//            alert("検索キーワードをご入力ください");
//            return false;
//        }else{
//            return true;
//        }
    }

    //選択された機器を記録する
    function saveSelectedForKiki(){
        //対象弁を記録
        var idList = "";
        $('.checkbox:checked').each(function(){
            idList = idList + $(this)[0].value + ',';
        });
        //Filter sessionに保存
        $.get("/valdacHost/item/saveSelectedForValve",{"idList":idList,"type":"2"},function(data){
            return true;
        });
    }
    //選択された機器を再秒化する
    function setSelectedForKiki(){
        var tmp = "${saveSelectedForKiki}";
        if(tmp.length>0){
            var tmp2=tmp.split(",");
            for(var i=0;i<tmp2.length-1;i++){
                var id=tmp2[i];
                document.getElementById(id).className="valve-item bg-light-blue-gradient";

                //checkout
                var tr = document.getElementById(id);
                var selected = $(tr).find('.checkbox');
                var checkbox = selected[0];
                $(checkbox).prop('checked', true);
            }
        }
    }

</script>

<c:import url="../htmlframe/footerFrame.jsp" />

</body>

</html>
