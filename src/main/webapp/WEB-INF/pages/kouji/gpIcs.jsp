<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 11/14/14
  Time: 4:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../htmlframe/headFrame.jsp" />
<html>
<style type="text/css">
input[type=checkbox] {
    /* checkbox 大さを調整する */
    width: 15px;
    height: 15px;
    vertical-align: middle;
}
</style>
<head>
    <title>工事支援</title>
</head>
<body class="skin-black">
    <!-- header logo: style can be found in header.less -->
    <c:import url="../htmlframe/headerFrame.jsp"/>
    <div class="container">
        <!-- Left side column. contains the logo and sidebar -->

        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                <i class="glyphicon glyphicon-wrench"> ${kouji.kjMeisyo}</i>
            </h1>
            <ol class="breadcrumb" style="font-size:20pt;">
                <li><i class="fa fa-dashboard"></i> <a href="/valdacHost/">工事一覧へ</a></li>
            </ol>
        </section>
        <hr/>

        <section class="content">

            <div class="row">
                <div class="col-md-11">
                    <div class="panel panel-success">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-md-10">
                                    <h4><i class="fa fa-download"></i> ダウンロード </h4>
                                </div>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-1">
                                    <form name="gp" action="/valdacHost/print/printGP/${kouji.id}/" id="valveListFormGP" method="post" onsubmit="checkGP();return false;">
                                        <input type="hidden" name="idList" id="idList-GP" value="" />
                                        <button class="btn btn-default" onclick="submitForm()">GP <i class="fa fa-print"></i></button>
                                    </form>
                                </div>
                                <div class="col-md-1">
                                    <form name="ics" action="/valdacHost/print/printICS/${kouji.id}/" id="valveListFormICS" method="post" onsubmit="checkICS();return false;">
                                        <input type="hidden" name="idList" id="idList-ICS" value="" />
                                        <button class="btn btn-default" onclick="submitForm()">ICS <i class="fa fa-print"></i></button>
                                    </form>
                                </div>
                                <div class="col-md-1">
                                    <form  name="TokuBetuKakoKirokuListDL" action="/valdacHost/print/printTokuBetuKakoKirokuForKikisysList/"  method="post" onsubmit="checkTokuBetuKakoKirokuListNull();return false;">
                                        <input type="hidden" name="idListTokuBetuKako" id="idListTokuBetuKako" value="" />
                                        <input type="hidden" name="koujiId" id="koujiId" value="${kouji.id}" />
                                        <input type="submit" class="btn btn-default" onclick="submitForm()" value="特別加工記録印刷"/>
                                    </form>
                                </div>
                                <div class="col-md-1"></div>
                                <div class="col-md-1">
                                    <form  name="KanriDaiTyoListDL" action="/valdacHost/print/printKanriDaiTyoForKikisysList/"  method="post" onsubmit="checkKanriDaiTyoListNull();return false;">
                                        <input type="hidden" name="idListKanriDaiTyo" id="idListKanriDaiTyo" value="" />
                                        <input type="hidden" name="koujiIdKanriDaiTyo" id="koujiIdKanriDaiTyo" value="${kouji.id}" />
                                        <input type="submit" class="btn btn-default" onclick="submitForm()" value="工場修理品入出荷管理台帳"/>
                                    </form>
                                </div>
                                <%--<div class="col-md-1">--%>
                                    <%--<form  name="SijisyoListDL" action="/valdacHost/print/printSijisyoForKikisysList"  method="post" onsubmit="checkSijisyoListNull();return false;">--%>
                                        <%--<input type="hidden" name="idListSijisyo" id="idListSijisyo" value="" />--%>
                                        <%--<input type="hidden" name="koujiIdSijisyo" id="koujiIdSijisyo" value="${kouji.id}" />--%>
                                        <%--<input type="submit" class="btn btn-default" onclick="submitForm()" value="指示書印刷"/>--%>
                                    <%--</form>--%>
                                <%--</div>--%>
                            </div>
                        </div>
                    </div>

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-md-10">
                                        <h4>弁リスト(弁を選択してから、ダウンロードのボタンを押してください)</h4>
                                        <p><font color="#ff0000">弁選択方法：弁番号または弁名称の上にクリックすると、選択/非選択できる</font></p><br>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-2">
                                        <div class="btn-group">
                                            <div class="input-group">
                                                <input type="text" name="table_search" id="table_search" class="form-control input-sm pull-right" style="width: 150px;" placeholder="Filter">
                                                <div class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="table_search_btn" onclick="filterPage()"><i class="fa fa-search"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-1"></div>
                                    <div class="col-md-1">
                                        <div class="btn-group">
                                            <div class="input-group">
                                                <button class="btn btn-sm btn-default selected_search_btn" id="selected_search_btn" onclick="selectedValveList()">選択された弁のみ表示</button>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-1"></div>
                                    <div class="col-md-1">
                                        <div class="btn-group">
                                            <div class="input-group">
                                                <button class="btn btn-sm btn-default" id="all_search_btn" onclick="allValveList()">全部弁表示</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <table class="table table-hover kiki-table select-table" id="kiki-table">
                                <thead>
                                <tr>
                                    <th><input type="checkbox" class="headCheckbox" onclick="selectAllItem()">(全選)</th>
                                    <th>弁番号</th>
                                    <th>弁名称</th>
                                    <th>ICS</th>
                                    <th>GP</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${tenkenValveList}" var="tenkenRirekiUtil">
                                    <tr class="valve-item" >
                                        <td><input type="checkbox" class="checkbox" style="opacity: 10" value="${tenkenRirekiUtil.valve.kikiSysId}"></td>
                                        <td>${tenkenRirekiUtil.valve.vNo}</td>
                                        <td>${tenkenRirekiUtil.valve.benMeisyo}</td>
                                        <td>${tenkenRirekiUtil.valve.ics}</td>
                                        <td><c:if test="${(tenkenRirekiUtil.valve.gpFlg eq '1')}">〇</c:if></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>

                </div><!-- information tab -->
                <div class="col-md-1">
                    <div class="row">
                        <div class="col-md-12">
                            <ul class="nav nav-pills nav-stacked bookmarkUl">
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}"><i class="glyphicon glyphicon-cog"> 情報</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/kenan"><i class="glyphicon glyphicon-floppy-save"> 懸案</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/history"><i class="glyphicon glyphicon-time"> 履歴</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/image"><i class="glyphicon glyphicon-picture"> 図面</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/printhtml"><i class="glyphicon glyphicon-download"> DL</i></a></li>
                                <li role="presentation" class="currentBookmark"><a href="/valdacHost/kouji/${kouji.id}/gpPrinthtml"><i class="glyphicon glyphicon-download"> GP&ICS</i></a></li>
                            </ul>
                        </div>
                    </div>
                </div><!-- tab button tab -->
            </div>
        </section>
    </div>
</body>

<script type="text/javascript">
    $(document).ready(function(){
        //ユーザ権限
        var userKengen=$("#userKengen").val();
        if(userKengen=="6" || userKengen=="5"){//管理者　責任者
            $(".kengen-operation").show();
        }else{
            $(".kengen-operation").hide();
        }

        $(".kiki-table tr").mouseover(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","1");
        });
        $(".kiki-table tr").mouseout(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","0");
        });

        //弁選択
        $('.valve-item').mouseup(function(obj){
            var tr = obj.currentTarget;
            var selected = $(tr).find('.checkbox');
            var checkbox = selected[0];
            if($(checkbox).prop("checked") == true){
                $(checkbox).prop('checked', false);
                $(tr).removeClass("bg-light-blue-gradient");
            } else {
                $(checkbox).prop('checked', true);
                $(tr).addClass("bg-light-blue-gradient");

            }
        });

    });
</script>
<script type="text/javascript">
    //Filter 手動
    function filterPage(){
        var keyword = $("#table_search").val();
        keyword=keyword.toLowerCase();
        keyword=keyword.trim();

        if(keyword.length > 0) {
            var trs = $(".kiki-table tr");
            for (var i = 0; i < trs.length; i++) {
                $(trs[i]).hide();
                //是否是头
                var ths = $(trs[i]).find("th");
                if(ths.length > 0){
                    $(trs[i]).show();
                    continue;
                }
                //不是头的行
                var tds = $(trs[i]).find("td");

                for (var j = 0; j < tds.length; j++) {
                    var tmpHtml = new String(tds[j].innerHTML);
                    if (tmpHtml.match(keyword)) {
                        $(trs[i]).show();
                        break;
                    }
                }
                //selected状態は表示されるように
                var selected = $(trs[i]).find('.checkbox');
                var checkbox = selected[0];
                if($(checkbox).prop("checked") == true){
                    $(trs[i]).show();
                }
            }
        } else {
            var trs = $(".kiki-table tr");
            for (var i = 0; i < trs.length; i++) {
                $(trs[i]).show();
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

    function submitForm(){
        var idList = "";
        $('.checkbox:checked').each(function(){
            idList = idList + $(this)[0].value + ',';
        });
        $('#idList-GP').val(idList);
        $('#idList-ICS').val(idList);
        $('#idListTokuBetuKako').val(idList);
        $('#idListKanriDaiTyo').val(idList);
//        $('#idListSijisyo').val(idList);
    }

//    ICS画像があるかどうかチェック
    function checkICS(){
        var idList = $("#idList-ICS").val();

        $.post("/valdacHost/print/checkPrintICS",{"idList":idList}, function(data){
            var isIcs = JSON.parse(data);
            if(isIcs) {
                document.ics.submit();
            }else{
                alert("ICSデータはありません。");
            }
        });
    }

    //    GP画像があるかどうかチェック
    function checkGP(){
        var idList = $("#idList-GP").val();

        $.post("/valdacHost/print/checkPrintGP",{"idList":idList}, function(data){
            var isGP = JSON.parse(data);
            if(isGP) {
                document.gp.submit();
            }else{
                alert("GPデータはありません。");
            }
        });
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
    //IdListは空場合はメッセージを表示する
    function checkKanriDaiTyoListNull(){
        var idList = $("#idListKanriDaiTyo").val();
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.KanriDaiTyoListDL.submit();
        }
    }
    //IdListは空場合はメッセージを表示する  この機能を破棄した
//    function checkSijisyoListNull(){
//        var idList = $("#idListSijisyo").val();
//        var idListArray=idList.split(",");
//        if(idList.length<1){
//            alert("弁を選択してから、ボタンを押してください。");
//        }else if(idListArray.length>11){
//            alert("弁件数を最大１０個にしてください。");
//        }else{
//            document.SijisyoListDL.submit();
//        }
//    }

</script>
</html>
